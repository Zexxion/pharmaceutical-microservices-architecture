package com.zexxion.pharmaceutical.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.zexxion.pharmaceutical.persistence.dto.MedicationDTO;
import java.util.List;

public interface MedicationsService {
    List<MedicationDTO> getMedications();
    MedicationDTO getMedication(final Integer medicationId);
    MedicationDTO saveMedication(final MedicationDTO medication);
    MedicationDTO updateMedication(final Integer medicationId, final MedicationDTO medication);
    MedicationDTO patchMedication(final Integer medicationId, final JsonPatch patch) throws JsonPatchException, JsonProcessingException;
    List<MedicationDTO> patchMedications(final List<Integer> medicationsIds, final JsonPatch patch) throws JsonPatchException, JsonProcessingException;
    void deleteMedication(final Integer medicationId);
}
