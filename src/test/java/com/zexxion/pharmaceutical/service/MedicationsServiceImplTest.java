package com.zexxion.pharmaceutical.service;

import com.zexxion.pharmaceutical.persistence.dto.MedicationDTO;
import com.zexxion.pharmaceutical.persistence.entities.Medication;
import com.zexxion.pharmaceutical.persistence.entities.MedicationStock;
import com.zexxion.pharmaceutical.persistence.entities.Producer;
import com.zexxion.pharmaceutical.persistence.entities.SideEffect;
import com.zexxion.pharmaceutical.persistence.repositories.MedicationsRepository;
import com.zexxion.pharmaceutical.persistence.repositories.MedicationsStockRepository;
import com.zexxion.pharmaceutical.serialization.mappers.MedicationModelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class MedicationsServiceImplTest {
    private MedicationsService medicationsService;

    @InjectMocks
    private MedicationModelMapper medicationModelMapper;

    @Mock
    private MedicationsRepository medicationsRepository;

    @Mock
    private MedicationsStockRepository medicationsStockRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        medicationsService = new MedicationsServiceImpl(medicationsRepository, medicationsStockRepository, medicationModelMapper);
    }

    @Test
    public void testGetMedicationsList() {
        final List<Medication> medicationsTest = getGeneratedMedications();
        given(medicationsRepository.findAll()).willReturn(medicationsTest);

        final List<Medication> medications = medicationsService.getMedications().stream()
                                                                                .map(medicationModelMapper::convertToEntity)
                                                                                .collect(Collectors.toList());

        assertThat(medications).isEqualTo(medicationsTest);
    }

    @Test
    public void testSaveMedication() {
        final Medication medicationTest = getGeneratedMedications().get(0);
        final MedicationDTO medicationTestDto = medicationModelMapper.convertToDTO(medicationTest);

        given(medicationsRepository.save(medicationTest)).willReturn(medicationTest);
        given(medicationsService.saveMedication(medicationTestDto)).willReturn(medicationTestDto);

        verify(medicationsRepository).save(medicationTest);
    }

    @Test
    public void testUpdateMedication() {
        final Medication medicationTest = getGeneratedMedications().get(0);
        final MedicationDTO medicationTestDto = medicationModelMapper.convertToDTO(medicationTest);

        given(medicationsRepository.save(medicationTest)).willReturn(medicationTest);
        given(medicationsService.updateMedication(medicationTestDto.getId(), medicationTestDto)).willReturn(medicationTestDto);

        verify(medicationsStockRepository).updateStock(medicationTestDto.getStock(), medicationTestDto.getId());
        verify(medicationsRepository).save(medicationTest);
    }

    private static List<Medication> getGeneratedMedications() {
        final Producer producer = new Producer(1, "Pfizer", "Bruxelles", "Belgium");
        final List<SideEffect> sideEffects = Collections.singletonList(new SideEffect(1, "AVC"));
        final Medication medication = new Medication(1, "Vaccin RHUME19", "Pas fort efficace...", 10, producer, sideEffects);
        final MedicationStock stock = new MedicationStock(medication, 25);
        medication.setStock(stock);


        return Collections.singletonList(new Medication(1,
                "Vaccin RHUME19",
                "Pas fort efficace...",
                10,
                producer,
                sideEffects,
                stock));
    }
}
