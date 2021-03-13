package com.example.demo.handler;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/files", method = {RequestMethod.GET, RequestMethod.POST})
public class FileUploadHandlers {

    // Mapping [POST] [multipart/form-data] /files
    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String, Object>> uploadFiles(MultipartFile file) {
        Map<String, Object> body = new HashMap<>();
        body.put("STATUS", file != null && !file.isEmpty() ? Boolean.TRUE : Boolean.FALSE);
        return ResponseEntity.ok(body);
    }

    // Resolve MultipartFile using RequestParamMethodArgumentResolver.
    @PostMapping(value = "/requestParam", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String, Object>> uploadFilesWithRequestParam(@RequestParam MultipartFile file) {
        return uploadFiles(file);
    }

    // Resolve MultipartFile using RequestParamMethodArgumentResolver.
    @PostMapping(value = "/requestPart", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String, Object>> uploadFilesWithRequestPart(@RequestPart MultipartFile file) {
        return uploadFiles(file);
    }

    // Resolve MultipartFile using RequestParamMethodArgumentResolver and ignore @RequestPart.
    @PostMapping(value = "/composite", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String, Object>> uploadFilesWithRequestParamOrPart(@RequestParam @RequestPart MultipartFile file) {
        return uploadFiles(file);
    }
}
