package com.yaeby.np_lab_2.controller;

import com.yaeby.np_lab_2.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class DataController {

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> upload(@RequestParam List<MultipartFile> files) {
        try {
            List<Map<String, String>> fileDataList = new ArrayList<>();
            for (MultipartFile file : files) {
                Map<String, String> fileData = new HashMap<>();
                fileData.put("filename", file.getOriginalFilename());

                String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
                fileData.put("imageData", base64Image);

                fileDataList.add(fileData);
            }
            return ResponseEntity.ok(new ApiResponse("Success", fileDataList));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("e.getMessage()", e.getMessage()));
        }
    }
}
