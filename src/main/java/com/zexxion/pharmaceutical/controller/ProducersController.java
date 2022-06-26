package com.zexxion.pharmaceutical.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.zexxion.pharmaceutical.persistence.dto.ProducerDTO;
import com.zexxion.pharmaceutical.service.ProducersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
@RequestMapping(name = "producers", path = "/producers")
@RestController
public class ProducersController {
    private final ProducersService producersService;

    @Operation(summary = "Get all existing producers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The list of all producers")
    })
    @GetMapping(path = "/")
    public ResponseEntity<List<ProducerDTO>> getProducers() {
        return ResponseEntity.ok(producersService.getProducers());
    }

    @Operation(summary = "Get a producer by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the producer"),
            @ApiResponse(responseCode = "404", description = "The producer was not found", content = @Content)
    })
    @GetMapping(path = "/{producer-id}")
    public ResponseEntity<ProducerDTO> getProducer(@PathVariable(name = "producer-id") final Integer producerId) {
        final ProducerDTO producer = producersService.getProducer(producerId);

        if (producer != null) {
            return ResponseEntity.ok(producersService.getProducer(producerId));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Save a new producer to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producer has been and saved in the database", content = @Content),
            @ApiResponse(responseCode = "500", description = "An internal server error occurred while saving the producer to the database", content = @Content)
    })
    @PostMapping(path = "/")
    public ResponseEntity<?> saveProducer(@RequestBody final ProducerDTO producer) {
        final ProducerDTO createdProducer = producersService.saveProducer(producer);

        if (createdProducer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            //TODO: Find a way to extract the resource url building procedure to a higher level
            final String resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                                                                       .path("/{producer-id}")
                                                                       .buildAndExpand(createdProducer.getId())
                                                                       .toUriString();

            return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, resourceLocation).build();
        }
    }

    //TODO: Add a JSON Patch example value for request body
    @Operation(summary = "Edit one or many fields of an existing producer using JSON Patch specification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producer has been successfully edited", content = @Content),
            @ApiResponse(responseCode = "500", description = "An internal server error occurred while editing the producer", content = @Content),
            @ApiResponse(responseCode = "400", description = "The provided JSON patch format is badly formatted", content = @Content)
    })
    @PatchMapping(path = "/{producer-id}", consumes = "application/json-patch+json")
    public ResponseEntity<?> patchProducer(@PathVariable(name = "producer-id") final Integer producerId, @RequestBody final JsonPatch patch) {
        try {
            final ProducerDTO patchedProducer = producersService.patchProducer(producerId, patch);

            if (patchedProducer == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @Operation(summary = "Replaces an existing producer with a new one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The producer has successfully be replaced", content = @Content),
            @ApiResponse(responseCode = "400", description = "The provided producer id was not found or the provided producer payload is badly formatted", content = @Content)
    })
    @PutMapping(path = "/{producer-id}")
    public ResponseEntity<?> updateProducer(@PathVariable(name = "producer-id") final Integer producerId, @RequestBody ProducerDTO producer) {
        final ProducerDTO updatedProducer = producersService.updateProducer(producerId, producer);

        if (updatedProducer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @Operation(summary = "Deletes an existing producer based on its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The producer was successfully deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "The producer does not exists or it has already been deleted", content = @Content)
    })
    @DeleteMapping(path = "{producer-id}")
    public ResponseEntity<?> deleteProducer(@PathVariable("producer-id") final Integer producerId) {
        producersService.deleteProducer(producerId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
