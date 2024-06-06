package org.example.controller;

import org.example.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
public class MeasurementController {

    private MeasurementService measurementService;

    @Autowired
    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @PostMapping("/measurement")
    public void uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            var reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            measurementService.uploadCsv(reader.lines());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/measurement")
    public String getAllData() {
        return measurementService.getAllData();
    }

    @GetMapping("/measurement/{code}")
    public String getMeasurementByCode(@PathVariable String code) {
        return measurementService.getByCode(code);
    }

    @DeleteMapping("/measurement")
    public void deleteAll() {
        measurementService.deleteAll();
    }
}
