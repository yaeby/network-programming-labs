package com.yaeby.np_lab_2.handler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandWebSocketHandler extends TextWebSocketHandler {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Path filePath = Path.of("data.txt");

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();

        if (payload.startsWith("write ")) {
            System.out.println("Writing data...");
            String textToWrite = payload.substring(6);
            executorService.submit(() -> writeFile(textToWrite));
        } else if (payload.equals("read")) {
            System.out.println("Reading data...");
            executorService.submit(() -> {
                String content = readFile();
                try {
                    session.sendMessage(new TextMessage(content));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void writeFile(String text) {
        try {
            Files.writeString(filePath, text, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFile() {
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading file";
        }
    }
}

