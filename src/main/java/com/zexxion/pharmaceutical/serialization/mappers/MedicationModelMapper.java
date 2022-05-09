package com.zexxion.pharmaceutical.serialization.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.zexxion.pharmaceutical.persistence.dto.MedicationDTO;
import com.zexxion.pharmaceutical.persistence.entities.Medication;
import org.modelmapper.ModelMapper;

public class MedicationModelMapper implements DomainModelMapper, DomainModelPatcher {
    @Override
    public MedicationDTO convertToDTO(final DomainEntity entity) {
        final Medication medicationEntity = (Medication) entity;
        return new ModelMapper().map(medicationEntity, MedicationDTO.class);
    }

    @Override
    public Medication convertToEntity(final DomainDTO dto) {
        final MedicationDTO medicationDTO = (MedicationDTO) dto;
        return new ModelMapper().map(medicationDTO, Medication.class);
    }

    @Override
    public MedicationDTO applyPatchToDto(final JsonPatch patch, final DomainDTO dto) throws JsonPatchException, JsonProcessingException {
        return (MedicationDTO) ModelPatcher.applyPatchToDto(patch, dto);
    }
}
