package com.zexxion.pharmaceutical.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.zexxion.pharmaceutical.persistence.dto.MedicationDTO;
import com.zexxion.pharmaceutical.persistence.entities.Medication;
import com.zexxion.pharmaceutical.persistence.entities.MedicationStock;
import com.zexxion.pharmaceutical.persistence.entities.Producer;
import com.zexxion.pharmaceutical.persistence.entities.SideEffect;
import com.zexxion.pharmaceutical.persistence.repositories.MedicationsRepository;
import com.zexxion.pharmaceutical.persistence.repositories.MedicationsStockRepository;
import com.zexxion.pharmaceutical.serialization.mapping.MedicationModelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
    public void Expect_GetMedications() {
        final List<Medication> medicationsTest = getMedications();
        given(medicationsRepository.findAll()).willReturn(medicationsTest);

        final List<Medication> medications = medicationsService.getMedications().stream()
                                                                                .map(medicationModelMapper::convertToEntity)
                                                                                .collect(Collectors.toList());

        assertThat(medications).isEqualTo(medicationsTest);
    }

    @Test
    public void When_IdentifierIsValid_Expect_GetMedication() {
        final Medication medicationEntity = getMedications().get(0);
        final MedicationDTO medication = medicationModelMapper.convertToDTO(medicationEntity);
        final Integer medicationId = medicationEntity.getId();

        given(medicationsRepository.findById(medicationId)).willReturn(Optional.of(medicationEntity));
        final MedicationDTO foundMedication = medicationsService.getMedication(medicationId);

        verify(medicationsRepository).findById(medicationId);
        assertThat(foundMedication).isEqualTo(medication);
    }

    @Test
    public void When_IdentifierIsInvalid_Expect_GetNoneMedication() {
        final Integer medicationId = 122;

        given(medicationsRepository.findById(medicationId)).willReturn(Optional.empty());
        final MedicationDTO nullMedication = medicationsService.getMedication(medicationId);

        assertThat(nullMedication).isNull();
    }

    @Test
    public void Expect_SaveMedication() {
        final Medication medicationTest = getMedication();
        final MedicationDTO medicationTestDto = medicationModelMapper.convertToDTO(medicationTest);

        given(medicationsStockRepository.save(medicationTest.getStock())).willReturn(medicationTest.getStock());
        given(medicationsRepository.save(medicationTest)).willReturn(medicationTest);
        final MedicationDTO savedMedicationDto = medicationsService.saveMedication(medicationTestDto);

        verify(medicationsRepository).save(medicationTest);
        assertThat(medicationTestDto).isEqualTo(savedMedicationDto);
    }

    @Test
    public void Expect_UpdateMedication() {
        final Medication medicationTest = getMedications().get(0);
        final MedicationDTO medicationTestDto = medicationModelMapper.convertToDTO(medicationTest);

        given(medicationsRepository.save(medicationTest)).willReturn(medicationTest);

        final MedicationDTO updatedMedication = medicationsService.updateMedication(medicationTestDto.getId(), medicationTestDto);

        verify(medicationsStockRepository).updateStock(medicationTestDto.getStock(), medicationTestDto.getId());
        verify(medicationsRepository).save(medicationTest);
        assertThat(medicationTestDto).isEqualTo(updatedMedication);
    }

    @Test
    public void When_IdentifierIsValid_Expect_PatchMedication() throws IOException, JsonPatchException {
        final String patchString = "[{\"op\":\"replace\",\"path\":\"/producer\",\"value\":502012},{\"op\":\"replace\",\"path\":\"/stock\",\"value\":210}]";
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode node = mapper.readTree(patchString);
        final JsonPatch patch = JsonPatch.fromJson(node);

        final Medication medicationEntity = getMedication();
        final MedicationDTO medication = medicationModelMapper.convertToDTO(medicationEntity);
        final MedicationDTO patchedMedication = medicationModelMapper.applyPatchToDto(patch, medication);
        final Medication patchedMedicationEntity = medicationModelMapper.convertToEntity(patchedMedication);
        final Integer medicationId = medicationEntity.getId();

        given(medicationsRepository.findById(medicationId)).willReturn(Optional.of(medicationEntity));
        given(medicationsRepository.save(patchedMedicationEntity)).willReturn(patchedMedicationEntity);
        final MedicationDTO savedPatchedMedication = medicationsService.patchMedication(medicationId, patch);

        verify(medicationsRepository).save(patchedMedicationEntity);
        verify(medicationsStockRepository).updateStock(any(Integer.class), any(Integer.class));
        assertThat(savedPatchedMedication).isEqualTo(patchedMedication);
    }

    @Test
    public void When_IdentifierIsInvalid_Expect_PatchMedicationToFail_ThrowResourceNotFoundException() {
        final Integer medicationId = 122;

        given(medicationsRepository.findById(medicationId)).willReturn(Optional.empty());

        final ResourceNotFoundException expectedException = assertThrows(ResourceNotFoundException.class, () -> medicationsService.patchMedication(medicationId, null), "ResourceNotFoundException was expected");
        assertThat(expectedException.getMessage().toLowerCase()).isEqualTo(String.format("Could not found a medication with id %d", medicationId).toLowerCase());
    }

    @Test
    public void When_JsonPatchIsInvalid_Expect_PatchMedicationToFail_ThrowInvalidTypeIdException() {
        final String invalidPatchString = "[{\"op\":\"replace\",\"path\":\"/invalidField\",\"value\":\"Paracétamol\"},{\"ope\":\"remove\",\"path\":\"/invalidField2\"}]";
        final ObjectMapper mapper = new ObjectMapper();

        assertThrows(InvalidTypeIdException.class, () -> {
            final JsonNode node = mapper.readTree(invalidPatchString);
            final JsonPatch patch = JsonPatch.fromJson(node);
            final Integer medicationId = 122;

            medicationsService.patchMedication(medicationId, patch);
        }, "InvalidTypeIdException was expected");
    }

    @Test
    public void Expect_DeleteMedication() {
        medicationsService.deleteMedication(any(Integer.class));

        verify(medicationsStockRepository).deleteByMedicationId(any(Integer.class));
        verify(medicationsRepository).deleteById(any(Integer.class));
    }

    private Medication getMedication() {
        return getMedications().get(0);
    }

    private List<Medication> getMedications() {
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
