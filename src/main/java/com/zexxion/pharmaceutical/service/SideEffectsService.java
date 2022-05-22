package com.zexxion.pharmaceutical.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.zexxion.pharmaceutical.persistence.dto.SideEffectDTO;
import java.util.List;

public interface SideEffectsService {
    List<SideEffectDTO> getSideEffects();
    SideEffectDTO getSideEffect(final Integer sideEffectId);
    SideEffectDTO saveSideEffect(final SideEffectDTO sideEffect);
    SideEffectDTO updateSideEffect(final Integer sideEffectId, final SideEffectDTO sideEffect);
    SideEffectDTO patchSideEffect(final Integer sideEffectId, final JsonPatch patch) throws JsonPatchException, JsonProcessingException;
}
