package com.zexxion.pharmaceutical.serialization.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import java.util.List;

public interface DomainModelPatcher {
    DomainDTO applyPatchToDto(final JsonPatch patch, final DomainDTO dto) throws JsonPatchException, JsonProcessingException;
    List<? extends DomainDTO> applyPatchToDtoList(final JsonPatch patch, final List<? extends DomainDTO> dtoList) throws JsonPatchException, JsonProcessingException;
}
