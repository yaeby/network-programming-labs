package com.yaeby.np_lab_2.udp;

import com.yaeby.np_lab_2.config.UdpConfig;
import com.yaeby.np_lab_2.model.RaftState;
import com.yaeby.np_lab_2.service.LeaderSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class RaftLeaderElectionService {

    private static final Logger logger = LoggerFactory.getLogger(RaftLeaderElectionService.class);
    private final LeaderSender sender;
    private final UdpConfig udpConfig;

    private static final int ELECTION_TIMEOUT = 10000;
    private static final int HEARTBEAT_INTERVAL = 5000;

    private RaftState currentState = RaftState.FOLLOWER;
    private final AtomicInteger currentTerm = new AtomicInteger(0);

    private Long lastHeartbeat = System.currentTimeMillis();
    private Integer votedFor = null;
    private Integer currentLeader = null;

    private final Set<Integer> discoveredNodes = new CopyOnWriteArraySet<>();
    private final Set<Integer> votedNodes = new CopyOnWriteArraySet<>();

    private static final String HEARTBEAT = "HEARTBEAT";
    private static final String RESPONSE_MESSAGE = "ELECTION";
    private static final String VOTE_REQUEST = "VOTE_REQUEST";
    private static final String VOTE_RESPONSE = "VOTE_RESPONSE";
    private static final String LEADER_ANNOUNCEMENT = "LEADER_ANNOUNCE";

    public void startLeaderElection() {
        Thread receiverThread = new Thread(this::receiveMessages);
        receiverThread.setName("Raft-Message-Receiver-Thread");
        receiverThread.start();

        Thread electionTimeoutThread = new Thread(this::electionTimeout);
        electionTimeoutThread.setName("Raft-Election-Timeout-Thread");
        electionTimeoutThread.start();

        logger.info("Node {} initialized.", udpConfig.getNodeId());
    }

    private void receiveMessages() {
        try (MulticastSocket socket = new MulticastSocket(udpConfig.getMULTICAST_PORT())) {
            InetAddress group = InetAddress.getByName(udpConfig.getMULTICAST_ADDRESS());
            socket.joinGroup(group);

            byte[] buffer = new byte[1024];

            while (!Thread.currentThread().isInterrupted()) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                processMessage(message);
            }
        } catch (Exception e) {
            logger.error("Message receive error", e);
        }
    }

    private void electionTimeout() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int timeout = ELECTION_TIMEOUT + udpConfig.getNodeId() * 500;
                Thread.sleep(timeout);

                Long currentTime = System.currentTimeMillis();
                if (currentState != RaftState.LEADER &&
                        (currentTime - lastHeartbeat) > timeout) {
                    startElection();
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void startElection() {
        currentTerm.incrementAndGet();
        currentState = RaftState.CANDIDATE;
        votedFor = udpConfig.getNodeId();
        votedNodes.clear();
        votedNodes.add(udpConfig.getNodeId());

        logger.info("Node {} starting election. state: {}. Term: {}",
                udpConfig.getNodeId(), currentState, currentTerm.get());

        broadcastMessage(VOTE_REQUEST);
    }

    private void processMessage(String message) {
        String[] parts = message.split(":");
        if (parts.length < 3) return;

        String messageType = parts[0];
        int senderId = Integer.parseInt(parts[1]);
        int senderTerm = Integer.parseInt(parts[2]);

        if (senderId == udpConfig.getNodeId()) return;

        discoveredNodes.add(senderId);

        if (senderTerm > currentTerm.get()) {
            logger.info("Node {} received higher term. Updating from {} to {}",
                    udpConfig.getNodeId(), currentTerm.get(), senderTerm);
            currentTerm.set(senderTerm);
            currentState = RaftState.FOLLOWER;
            votedFor = null;
        }

        switch (messageType) {
            case VOTE_REQUEST:
                handleVoteRequest(senderId, senderTerm);
                break;
            case VOTE_RESPONSE:
                handleVoteResponse(senderId);
                break;
            case LEADER_ANNOUNCEMENT:
                handleLeaderAnnouncement(senderId);
                break;
            case HEARTBEAT:
                handleHeartbeat(senderId);
                break;
        }
    }

    private void handleHeartbeat(int senderId) {
        if (currentState != RaftState.LEADER) {
            logger.info("Node {} received heartbeat from Node {}. Resetting election timeout.",
                    udpConfig.getNodeId(), senderId);
            currentLeader = senderId;
            currentState = RaftState.FOLLOWER;
            lastHeartbeat = System.currentTimeMillis();
            broadcastMessage(RESPONSE_MESSAGE);
        }
    }

    private void handleVoteRequest(int candidateId, int candidateTerm) {
        if (votedFor == null && candidateTerm >= currentTerm.get()) {
            votedFor = candidateId;
            broadcastMessage(VOTE_RESPONSE);
            logger.info("Node {} voted for Node {} in term {}",
                    udpConfig.getNodeId(), candidateId, candidateTerm);
        } else {
            logger.debug("Node {} rejected vote request from Node {}. Voted for: {}, Current term: {}, Candidate term: {}",
                    udpConfig.getNodeId(), candidateId, votedFor, currentTerm.get(), candidateTerm);
        }
    }

    private void handleVoteResponse(int voterId) {
        if (currentState == RaftState.CANDIDATE) {
            votedNodes.add(voterId);

            logger.info("Node {} received vote from Node {}. Votes: {}/{}",
                    udpConfig.getNodeId(), voterId, votedNodes.size(), discoveredNodes.size() + 1);

            if (votedNodes.size() > (discoveredNodes.size() + 1) / 2) {
                becomeLeader();
            }
        }
    }

    private void becomeLeader() {
        currentState = RaftState.LEADER;
        currentLeader = udpConfig.getNodeId();

        logger.info("Node {} BECAME THE LEADER in term {}!", udpConfig.getNodeId(), currentTerm.get());

        startHeartbeatThread();
        broadcastMessage(LEADER_ANNOUNCEMENT);
        sender.sendNewLeader();
    }

    private void handleLeaderAnnouncement(int leaderId) {
        currentState = RaftState.FOLLOWER;
        currentLeader = leaderId;
        logger.info("Node {} now follows Node {} as leader", udpConfig.getNodeId(), currentLeader);
    }

    private void startHeartbeatThread() {
        Thread heartbeatThread = new Thread(() -> {
            try {
                while (currentState == RaftState.LEADER) {
                    broadcastMessage(HEARTBEAT);
                    logger.debug("Node {} send heartbeat. Term: {}", udpConfig.getNodeId(), currentTerm.get());
                    Thread.sleep(HEARTBEAT_INTERVAL);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Heartbeat thread error", e);
            }
        });
        heartbeatThread.setName("Raft-Heartbeat-Thread");
        heartbeatThread.start();
    }

    private void broadcastMessage(String type){
        try (MulticastSocket socket = new MulticastSocket()) {
            InetAddress group = InetAddress.getByName(udpConfig.getMULTICAST_ADDRESS());

            String message = String.format("%s:%d:%d",
                    type,
                    udpConfig.getNodeId(),
                    currentTerm.get()
            );

            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(
                    buffer,
                    buffer.length,
                    group,
                    udpConfig.getMULTICAST_PORT()
            );

            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void init() {
        startLeaderElection();
    }
}