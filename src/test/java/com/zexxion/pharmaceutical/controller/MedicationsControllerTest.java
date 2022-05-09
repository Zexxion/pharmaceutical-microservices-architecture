package com.zexxion.pharmaceutical.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zexxion.pharmaceutical.persistence.entities.Medication;
import com.zexxion.pharmaceutical.persistence.entities.MedicationStock;
import com.zexxion.pharmaceutical.persistence.entities.Producer;
import com.zexxion.pharmaceutical.persistence.entities.SideEffect;
import com.zexxion.pharmaceutical.service.MedicationsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@WebMvcTest(MedicationsController.class)
public class MedicationsControllerTest {
    @MockBean
    private MedicationsService medicationsService;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<List<Medication>> jsonMedications;

    @BeforeEach
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    /* @Test
    public void testGetMedicationsList() throws Exception {
        final List<Medication> medicationsTest = getGeneratedMedications();
        given(medicationsService.getMedications()).willReturn(medicationsTest);

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/medications/")
                                                                    .contentType(MediaType.APPLICATION_JSON)
                                                                    .content(jsonMedications.write(medicationsTest).getJson());
        final MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonMedications.write(medicationsTest).getJson());
    }

    private static List<Medication> getGeneratedMedications() {
        final Producer producer = new Producer(1, "Pfizer", "Bruxelles", "Belgium");
        final List<SideEffect> sideEffects = Collections.singletonList(new SideEffect(1, "AVC"));
        final MedicationStock stock = new MedicationStock(1, 25);

        return Collections.singletonList(new Medication(1,
                "Vaccin RHUME19",
                "Pas fort efficace...",
                10,
                producer,
                sideEffects,
                stock));
    } */
}
