package com.zexxion.pharmaceutical.serialization.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.zexxion.pharmaceutical.persistence.dto.ProducerDTO;
import com.zexxion.pharmaceutical.persistence.entities.Producer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

public class ProducerModelMapper implements DomainModelMapper, DomainModelPatcher {
    @Override
    public ProducerDTO convertToDTO(DomainEntity entity) {
        final Producer producerEntity = (Producer) entity;
        return new ModelMapper().map(producerEntity, ProducerDTO.class);
    }

    @Override
    public Producer convertToEntity(DomainDTO dto) {
        final ProducerDTO producerDTO = (ProducerDTO) dto;
        return new ModelMapper().map(producerDTO, Producer.class);
    }

    @Override
    public ProducerDTO applyPatchToDto(JsonPatch patch, DomainDTO dto) throws JsonPatchException, JsonProcessingException {
        return (ProducerDTO) ModelPatcher.applyPatchToDto(patch, dto);
    }
}
