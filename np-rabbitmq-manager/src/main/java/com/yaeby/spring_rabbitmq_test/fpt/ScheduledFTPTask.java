package com.yaeby.spring_rabbitmq_test.fpt;

import com.yaeby.spring_rabbitmq_test.email.EmailSenderService;
import com.yaeby.spring_rabbitmq_test.publisher.Sender;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class ScheduledFTPTask {

    private final FTPClientService ftpClientService;
    private static final String LOCAL_FILE_PATH = "data.json";
    private final Sender sender;
    private final EmailSenderService emailSenderService;

    @Scheduled(fixedRate = 30000)
    public void fetchAndUploadFile() {
        ftpClientService.downloadFile("data.json", LOCAL_FILE_PATH);
        sender.sendMultipartFileRequest(new File(LOCAL_FILE_PATH));
        emailSenderService.sendSimpleEmail(
                "jana09sokolova@gmail.com",
                "FTP server",
                "Data from the ftp server has been fetched successfully"
        );
    }
}