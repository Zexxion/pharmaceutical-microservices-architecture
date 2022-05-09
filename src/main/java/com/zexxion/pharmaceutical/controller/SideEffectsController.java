package com.zexxion.pharmaceutical.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.zexxion.pharmaceutical.persistence.dto.SideEffectDTO;
import com.zexxion.pharmaceutical.service.SideEffectsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(name = "side_effects", path = "/side-effects")
public class SideEffectsController {
    private final SideEffectsService sideEffectsService;

    @GetMapping(path = "/")
    public ResponseEntity<List<SideEffectDTO>> getSideEffects() {
        return ResponseEntity.ok(sideEffectsService.getSideEffects());
    }

    @GetMapping(path = "{side-effect-id}")
    public ResponseEntity<SideEffectDTO> getSideEffect(@PathVariable(name = "side-effect-id") final Integer sideEffectId) {
        return ResponseEntity.ok(sideEffectsService.getSideEffect(sideEffectId));
    }

    @PostMapping(path = "/")
    public ResponseEntity<?> saveSideEffect(@RequestBody final SideEffectDTO sideEffect) {
        final SideEffectDTO createdSideEffect = sideEffectsService.saveSideEffect(sideEffect);

        if (createdSideEffect == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            final String resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                                                                       .path("/{side-effect-id}")
                                                                       .buildAndExpand(createdSideEffect.getId())
                                                                       .toUriString();
            return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, resourceLocation).build();
        }
    }

    @PatchMapping(path = "/{side-effect-id}")
    public ResponseEntity<?> patchSideEffect(@PathVariable(name = "side-effect-id") final Integer sideEffectId, @RequestBody final JsonPatch patch) {
        try {
            sideEffectsService.patchSideEffect(sideEffectId, patch);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping(path = "/{side-effect-id}")
    public ResponseEntity<?> updateSideEffect(@PathVariable(name = "side-effect-id") final Integer sideEffectId, @RequestBody final SideEffectDTO sideEffect) {
        final SideEffectDTO updatedSideEffect = sideEffectsService.updateSideEffect(sideEffectId, sideEffect);

        if (updatedSideEffect == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
