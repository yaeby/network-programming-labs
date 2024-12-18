package com.yaeby.np_lab_2.handler;

import com.yaeby.np_lab_2.model.Command;
import com.yaeby.np_lab_2.model.CommandType;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.*;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CommandWebSocketHandler extends TextWebSocketHandler {
    private static final String FILE_PATH = "src/output/data.txt";
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final PriorityBlockingQueue<Command> commandQueue = new PriorityBlockingQueue<>();

    public CommandWebSocketHandler() {
        new Thread(this::processQueue).start();
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {

        String payload = message.getPayload();
        System.out.println("Client command: " + payload);

        if (payload.startsWith("write: ")) {
            System.out.println("Write command pushed to execution queue");
            commandQueue.add(new Command(() -> processMessage(payload, session), CommandType.WRITE));
        } else if (payload.startsWith("read")) {
            System.out.println("Read command pushed to execution queue");
            commandQueue.add(new Command(() -> processMessage(payload, session), CommandType.READ));
        } else {
            sendMessage(session, "Unknown command");
        }
    }

    private void processQueue() {
        while (true) {
            try {
                Command command = commandQueue.take();
                System.out.println("Processing " + command.getCommandType() + " command");
                command.run();
                Thread.sleep(50);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Queue processing interrupted");
                break;
            }
        }
    }

    private void processMessage(String message, WebSocketSession session) {
        if (message.startsWith("write: ")) {
            String data = message.substring(message.indexOf("write: ") + 7);
            if (data.isEmpty()) {
                sendMessage(session, "Empty data");
            } else {
                writeFile(data).thenRun(() -> sendMessage(session, "Write operation completed"));
            }
        } else if (message.startsWith("read")) {
            readFile().thenAccept(content -> sendMessage(session, "Read operation completed: " + content));
        } else {
            sendMessage(session, "Unknown command");
        }
    }

    private CompletableFuture<Void> writeFile(String data) {
        return CompletableFuture.runAsync(() -> {
            lock.writeLock().lock();
            try {
                simulateRandomDelay();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                    writer.write(data);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                lock.writeLock().unlock();
            }
        });
    }

    private CompletableFuture<String> readFile() {
        return CompletableFuture.supplyAsync(() -> {
            lock.readLock().lock();
            StringBuilder content = new StringBuilder();
            try {
                simulateRandomDelay();
                try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                lock.readLock().unlock();
            }
            return content.toString();
        });
    }

    private void simulateRandomDelay() {
        try {
            int sleepTime = 3000 + new Random().nextInt(10000);
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void sendMessage(WebSocketSession session, String message) {
        try {
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("Connection closed: " + status);
    }
}

