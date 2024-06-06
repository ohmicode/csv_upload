package org.example.controller;

import org.example.service.MeasurementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileInputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeasurementController.class)
public class MeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeasurementService measurementService;

    @Test
    public void testUploadCsv() throws Exception {
        FileInputStream input = new FileInputStream(new ClassPathResource("exercise.csv").getFile());
        MockMultipartFile file = new MockMultipartFile("file", "exercise.csv", "text/csv", input);

        doNothing().when(measurementService).uploadCsv(any());

        mockMvc.perform(multipart("/measurement").file(file))
            .andExpect(status().isOk());

        verify(measurementService, times(1)).uploadCsv(any());
    }

    @Test
    public void testGetMeasurementByCode() throws Exception {
        String code = "testCode";
        String expectedData = "expected data";
        given(measurementService.getByCode(code)).willReturn(expectedData);

        mockMvc.perform(get("/measurement/" + code))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedData));

        verify(measurementService, times(1)).getByCode(code);
    }

    @Test
    public void testDeleteAll() throws Exception {
        doNothing().when(measurementService).deleteAll();

        mockMvc.perform(delete("/measurement"))
            .andExpect(status().isOk());

        verify(measurementService, times(1)).deleteAll();
    }
}
