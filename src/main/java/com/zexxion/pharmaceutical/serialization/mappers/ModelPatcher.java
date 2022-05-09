package com.zexxion.pharmaceutical.serialization.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

class ModelPatcher {
    protected static DomainDTO applyPatchToDto(JsonPatch patch, DomainDTO dto) throws JsonPatchException, JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode patchedDto = patch.apply(mapper.convertValue(dto, JsonNode.class));

        return mapper.treeToValue(patchedDto, dto.getClass());
    }
}
