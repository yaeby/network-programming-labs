package com.yaeby.spring_rabbitmq_test.fpt;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;

@Service
public class FTPClientService {

    @Value("${ftp.host}")
    private String FTP_HOST ;

    @Value("${ftp.port}")
    private int FTP_PORT;

    @Value("${ftp.user}")
    private String FTP_USER;

    @Value("${ftp.password}")
    private String FTP_PASS;

    public void downloadFile(String remoteFilePath, String localFilePath) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FTP_HOST, FTP_PORT);
            ftpClient.login(FTP_USER, FTP_PASS);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            try (FileOutputStream outputStream = new FileOutputStream(localFilePath)) {
                ftpClient.retrieveFile(remoteFilePath, outputStream);
            }

            ftpClient.logout();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ftpClient.disconnect();
            } catch (Exception ignored) {
            }
        }
    }
}
