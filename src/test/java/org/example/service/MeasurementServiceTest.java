package org.example.service;

import org.example.model.MeasurementEntity;
import org.example.model.MeasurementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MeasurementServiceTest {

    private MeasurementService measurementService;

    @Mock
    private MeasurementRepository measurementRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        measurementService = new MeasurementService(measurementRepository);
    }

    @Test
    public void testGetAllData() {
        List<MeasurementEntity> repositoryData = new ArrayList<>();
        repositoryData.add(createOneEntity("code1"));
        repositoryData.add(createOneEntity("code2"));
        var expectedData = "\"source\",\"codeListCode\",\"code\",\"displayValue\",\"longDescription\",\"fromDate\",\"toDate\",\"sortingPriority\"\n" +
                "\"\",\"\",\"code1\",\"\",\"\",\"\",\"\",\"\"\n" +
                "\"\",\"\",\"code2\",\"\",\"\",\"\",\"\",\"\"\n";
        when(measurementRepository.findAll()).thenReturn(repositoryData);

        var actualData = measurementService.getAllData();

        assertEquals(expectedData, actualData);
        verify(measurementRepository, times(1)).findAll();
    }

    @Test
    public void testGetByCode() {
        String code = "testCode";
        MeasurementEntity expectedEntity = createOneEntity(code);
        String expectedData = "\"source\",\"codeListCode\",\"code\",\"displayValue\",\"longDescription\",\"fromDate\",\"toDate\",\"sortingPriority\"\n" +
            "\"\",\"\",\"" + code + "\",\"\",\"\",\"\",\"\",\"\"\n";
        when(measurementRepository.findById(code)).thenReturn(Optional.of(expectedEntity));

        String actualData = measurementService.getByCode(code);

        assertEquals(expectedData, actualData);
        verify(measurementRepository, times(1)).findById(code);
    }

    @Test
    public void testGetByCodeNotFound() {
        String code = "testCode";
        String expectedData = "\"source\",\"codeListCode\",\"code\",\"displayValue\",\"longDescription\",\"fromDate\",\"toDate\",\"sortingPriority\"\n\n";
        when(measurementRepository.findById(code)).thenReturn(Optional.empty());

        String actualData = measurementService.getByCode(code);

        assertEquals(expectedData, actualData);
        verify(measurementRepository, times(1)).findById(code);
    }

    @Test
    public void testDeleteAll() {
        doNothing().when(measurementRepository).deleteAll();

        measurementService.deleteAll();

        verify(measurementRepository, times(1)).deleteAll();
    }

    @Test
    public void testUploadCsv() throws IOException {
        FileInputStream input = new FileInputStream(new ClassPathResource("exercise.csv").getFile());
        var reader = new BufferedReader(new InputStreamReader(input));

        measurementService.uploadCsv(reader.lines());

        verify(measurementRepository, times(18)).save(any(MeasurementEntity.class));
    }

    private MeasurementEntity createOneEntity(String code) {
        var entity = new MeasurementEntity();
        entity.setCode(code);
        entity.setSource("");
        entity.setCodeListCode("");
        entity.setDisplayValue("");
        entity.setLongDescription("");
        entity.setFromDate("");
        entity.setToDate("");
        entity.setSortingPriority("");
        return entity;
    }
}
