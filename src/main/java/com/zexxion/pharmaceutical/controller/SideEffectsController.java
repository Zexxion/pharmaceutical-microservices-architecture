package com.zexxion.pharmaceutical.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.zexxion.pharmaceutical.persistence.dto.SideEffectDTO;
import com.zexxion.pharmaceutical.service.SideEffectsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all existing side effects")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The list of all side effects", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SideEffectDTO.class))))
    })
    @GetMapping(path = "/")
    public ResponseEntity<List<SideEffectDTO>> getSideEffects() {
        return ResponseEntity.ok(sideEffectsService.getSideEffects());
    }

    @Operation(summary = "Get a side effect by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the side effect", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SideEffectDTO.class))),
        @ApiResponse(responseCode = "404", description = "The side effect was not fond", content = @Content)
    })
    @GetMapping(path = "{side-effect-id}")
    public ResponseEntity<SideEffectDTO> getSideEffect(@PathVariable(name = "side-effect-id") final Integer sideEffectId) {
        return ResponseEntity.ok(sideEffectsService.getSideEffect(sideEffectId));
    }

    @Operation(summary = "Save a new side effect to the database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Side effect has been saved to the database", content = @Content, headers = @Header(name = HttpHeaders.LOCATION, description = "The access URL of the newly created side effect")),
        @ApiResponse(responseCode = "500", description = "An internal server error occurred while saving the side effect to the database", content = @Content)
    })
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

    @Operation(summary = "Edit one or many fields of an existing side effect using JSON Patch specification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Side effect has been successfully edited", content = @Content),
            @ApiResponse(responseCode = "500", description = "An internal server error occurred while editing the side effect", content = @Content),
            @ApiResponse(responseCode = "400", description = "The provided JSON patch format is badly formatted", content = @Content)
    })
    @PatchMapping(path = "/{side-effect-id}")
    public ResponseEntity<?> patchSideEffect(@PathVariable(name = "side-effect-id") final Integer sideEffectId, @RequestBody final JsonPatch patch) {
        try {
            sideEffectsService.patchSideEffect(sideEffectId, patch);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Replaces an existing side effect with a new one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The side effect has successfully be replaced", content = @Content),
            @ApiResponse(responseCode = "400", description = "The provided side effect id was not found or the provided side effect payload is badly formatted", content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The side effect to save into the database", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SideEffectDTO.class)))
    @PutMapping(path = "/{side-effect-id}")
    public ResponseEntity<?> updateSideEffect(@PathVariable(name = "side-effect-id") final Integer sideEffectId, @RequestBody final SideEffectDTO sideEffect) {
        final SideEffectDTO updatedSideEffect = sideEffectsService.updateSideEffect(sideEffectId, sideEffect);

        if (updatedSideEffect == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @Operation(summary = "Deletes an existing side effect based on its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The side effect was successfully deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "The side effect does not exists or it has already been deleted", content = @Content)
    })
    @DeleteMapping(path = "/{side-effect-id}")
    public ResponseEntity<?> deleteSideEffect(@PathVariable(name = "side-effect-id") final Integer sideEffectId) {
        sideEffectsService.deleteSideEffect(sideEffectId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
