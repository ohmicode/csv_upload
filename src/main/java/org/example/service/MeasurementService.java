package org.example.service;

import org.example.model.MeasurementEntity;
import org.example.model.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class MeasurementService {

    private static final String HEADER = "\"source\",\"codeListCode\",\"code\",\"displayValue\",\"longDescription\",\"fromDate\",\"toDate\",\"sortingPriority\"";

    private final MeasurementRepository measurementRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public void uploadCsv(Stream<String> csv) {
        csv.forEach(line -> {
            if (!isHeader(line)) {
                parseAndSave(line);
            }
        });
    }

    public String getAllData() {
        var all = measurementRepository.findAll();
        var builder = new StringBuilder(HEADER).append("\n");
        all.forEach(measurementEntity -> builder.append(entityToRow(measurementEntity)).append("\n"));
        return builder.toString();
    }

    public String getByCode(String code) {
        var entity = measurementRepository.findById(code);
        var builder = new StringBuilder(HEADER).append("\n");
        builder.append(entity.map(this::entityToRow).orElse("")).append("\n");
        return builder.toString();
    }

    public void deleteAll() {
        measurementRepository.deleteAll();
    }

    private boolean isHeader(String line) {
        return line.startsWith(HEADER);
    }

    private void parseAndSave(String line) {
        var entity = new MeasurementEntity();
        var parts = line.split(",");
        entity.setSource(trimQuotes(parts[0]));
        entity.setCodeListCode(trimQuotes(parts[1]));
        entity.setCode(trimQuotes(parts[2]));
        entity.setDisplayValue(trimQuotes(parts[3]));
        entity.setLongDescription(trimQuotes(parts[4]));
        entity.setFromDate(trimQuotes(parts[5]));
        entity.setToDate(trimQuotes(parts[6]));
        entity.setSortingPriority(trimQuotes(parts[7]));
        measurementRepository.save(entity);
    }

    private String trimQuotes(String s) {
        return s.substring(1, s.length() - 1);
    }

    private String entityToRow(MeasurementEntity entity) {
        StringBuilder builder = new StringBuilder();
        builder.append("\"").append(entity.getSource()).append("\",")
            .append("\"").append(entity.getCodeListCode()).append("\",")
            .append("\"").append(entity.getCode()).append("\",")
            .append("\"").append(entity.getDisplayValue()).append("\",")
            .append("\"").append(entity.getLongDescription()).append("\",")
            .append("\"").append(entity.getFromDate()).append("\",")
            .append("\"").append(entity.getToDate()).append("\",")
            .append("\"").append(entity.getSortingPriority()).append("\"");
        return builder.toString();
    }
}
