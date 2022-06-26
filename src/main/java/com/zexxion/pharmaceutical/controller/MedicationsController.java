package com.zexxion.pharmaceutical.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.zexxion.pharmaceutical.persistence.dto.MedicationDTO;
import com.zexxion.pharmaceutical.service.MedicationsService;
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
@RequestMapping(name = "medications", path = "/medications")
@RestController
public class MedicationsController {
    public final MedicationsService medicationsService;

    @Operation(summary = "Get all existing medications")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The list of all medications")
    })
    @GetMapping(path = "/")
    public ResponseEntity<List<MedicationDTO>> getMedications() {
        return ResponseEntity.ok(medicationsService.getMedications());
    }

    @Operation(summary = "Get a medication by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the medication"),
        @ApiResponse(responseCode = "404", description = "The medication was not found", content = @Content)
    })
    @GetMapping(path = "/{medication-id}")
    public ResponseEntity<MedicationDTO> getMedication(@PathVariable("medication-id") final Integer medicationId) {
        final MedicationDTO medication = medicationsService.getMedication(medicationId);

        if (medication != null) {
            return ResponseEntity.ok(medicationsService.getMedication(medicationId));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Save a new medication to the database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Medication has been and saved in the database", content = @Content),
        @ApiResponse(responseCode = "500", description = "An internal server error occurred while saving the medication to the database", content = @Content)
    })
    @PostMapping(path = "/")
    public ResponseEntity<?> saveMedication(@RequestBody final MedicationDTO medication) {
        final MedicationDTO createdMedication = medicationsService.saveMedication(medication);

        if (createdMedication == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } else {
            //TODO: Find a way to extract the resource url building procedure to a higher level
            final String resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                                                                    .path("/{medication-id}")
                                                                    .buildAndExpand(createdMedication.getId())
                                                                    .toUriString();
            return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, resourceLocation).build();
        }
    }

    //TODO: Add a JSON Patch example value for request body
    @Operation(summary = "Edit one or many fields of an existing medication using JSON Patch specification")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Medication has been successfully edited", content = @Content),
        @ApiResponse(responseCode = "500", description = "An internal server error occurred while editing the medication", content = @Content),
        @ApiResponse(responseCode = "400", description = "The provided JSON patch format is badly formatted", content = @Content)
    })
    @PatchMapping(path = "/{medication-id}", consumes = "application/json-patch+json")
    public ResponseEntity<?> patchMedication(@PathVariable(value = "medication-id") final Integer medicationId, @RequestBody final JsonPatch patch) {
        try {
            final MedicationDTO patchedMedication = medicationsService.patchMedication(medicationId, patch);

            if (patchedMedication == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Replaces an existing medication with a new one")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "The medication has successfully be replaced", content = @Content),
        @ApiResponse(responseCode = "400", description = "The provided medication id was not found or the provided medication payload is badly formatted", content = @Content)
    })
    @PutMapping(path = "/{medication-id}")
    public ResponseEntity<?> updateMedication(@PathVariable("medication-id") final Integer medicationId,
                                              @RequestBody final MedicationDTO medication) {
        final MedicationDTO updatedMedication = medicationsService.updateMedication(medicationId, medication);

        if (updatedMedication == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @Operation(summary = "Deletes an existing medication based on its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "The medication was successfully deleted", content = @Content),
        @ApiResponse(responseCode = "404", description = "The medication does not exists or it has already been deleted", content = @Content)
    })
    @DeleteMapping(path = "{medication-id}")
    public ResponseEntity<?> deleteMedication(@PathVariable("medication-id") final Integer medicationId) {
        final MedicationDTO medication = medicationsService.getMedication(medicationId);

        if (medication != null) {
            medicationsService.deleteMedication(medicationId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
}
