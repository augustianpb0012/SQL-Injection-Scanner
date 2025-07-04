package com.example.sqlscanner.controller;

import com.example.sqlscanner.service.ScannerService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ScannerController {

    private final ScannerService scannerService;

    public ScannerController(ScannerService scannerService) {
        this.scannerService = scannerService;
    }

    @PostMapping("/scan")
    public Map<String, Object> scanUrl(@RequestBody Map<String, String> request) {
        String url = request.get("url");
        Map<String, Object> result = new HashMap<>();

        try {
            List<Map<String, String>> findings = scannerService.scanUrl(url);
            result.put("status", "success");
            result.put("vulnerabilities", findings);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }

        return result;
    }
}
