package com.zexxion.pharmaceutical.serialization.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import java.util.List;

class ModelPatcher {
    protected static DomainDTO applyPatchToDto(final JsonPatch patch, final DomainDTO dto) throws JsonPatchException, JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode patchedDto = patch.apply(mapper.convertValue(dto, JsonNode.class));

        return mapper.treeToValue(patchedDto, dto.getClass());
    }

    protected static List<? extends DomainDTO> applyPatchToDtoList(final JsonPatch patch, final List<? extends DomainDTO> dtoList) throws JsonPatchException, JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode patchedDto = patch.apply(mapper.convertValue(dtoList, JsonNode.class));

        return mapper.treeToValue(patchedDto, dtoList.getClass());
    }
}
