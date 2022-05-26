package com.zexxion.pharmaceutical.serialization.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.zexxion.pharmaceutical.persistence.dto.MedicationDTO;
import com.zexxion.pharmaceutical.persistence.entities.Medication;
import org.modelmapper.ModelMapper;
import java.util.List;

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

    @Override
    public List<? extends DomainDTO> applyPatchToDtoList(JsonPatch patch, List<? extends DomainDTO> dtoList) throws JsonPatchException, JsonProcessingException {
        return ModelPatcher.applyPatchToDtoList(patch, dtoList);
    }
}
