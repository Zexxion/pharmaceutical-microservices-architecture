package com.zexxion.pharmaceutical.serialization.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.zexxion.pharmaceutical.persistence.dto.SideEffectDTO;
import com.zexxion.pharmaceutical.persistence.entities.SideEffect;
import org.modelmapper.ModelMapper;

import java.util.List;

public class SideEffectModelMapper implements DomainModelMapper, DomainModelPatcher {
    @Override
    public SideEffectDTO convertToDTO(DomainEntity entity) {
        final SideEffect sideEffectEntity = (SideEffect) entity;

        return new ModelMapper().map(sideEffectEntity, SideEffectDTO.class);
    }

    @Override
    public SideEffect convertToEntity(DomainDTO dto) {
        final SideEffectDTO sideEffectDTO = (SideEffectDTO) dto;

        return new ModelMapper().map(sideEffectDTO, SideEffect.class);
    }

    @Override
    public SideEffectDTO applyPatchToDto(JsonPatch patch, DomainDTO dto) throws JsonPatchException, JsonProcessingException {
        return (SideEffectDTO) ModelPatcher.applyPatchToDto(patch, dto);
    }

    @Override
    public List<? extends DomainDTO> applyPatchToDtoList(JsonPatch patch, List<? extends DomainDTO> dtoList) throws JsonPatchException, JsonProcessingException {
        return null;
    }
}
