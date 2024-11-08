package com.yaeby.np_lab_2.handler;

import com.yaeby.np_lab_2.model.Command;
import com.yaeby.np_lab_2.model.CommandType;

import java.util.concurrent.PriorityBlockingQueue;

public class QueueProcessor {
    private final PriorityBlockingQueue<Command> CommandQueue = new PriorityBlockingQueue<>();

    // Method to add Commands to the priority queue
    public void addCommand(Runnable command, CommandType commandType) {
        System.out.println("Received: " + commandType);
        CommandQueue.add(new Command(command, commandType));
        System.out.println("Command queue size: " + CommandQueue.size());
    }

    // Main queue processing loop
    public void processQueue() {
        while (true) {
            try {
                Command Command = CommandQueue.take(); // Processes Commands based on priority
                System.out.println("Processing " + Command.getCommandType() + " Command");
                Command.run();
                System.out.println("Command queue size: " + CommandQueue.size());

                Thread.sleep(50); // Optional: Simulate Command processing time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Queue processing interrupted");
                break;
            }
        }
    }
}
