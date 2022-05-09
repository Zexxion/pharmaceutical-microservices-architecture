package com.zexxion.pharmaceutical.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.zexxion.pharmaceutical.persistence.dto.MedicationDTO;
import com.zexxion.pharmaceutical.service.MedicationsService;
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

    @GetMapping(path = "/")
    public ResponseEntity<List<MedicationDTO>> getMedications() {
        return ResponseEntity.ok(medicationsService.getMedications());
    }

    @GetMapping(path = "/{medication-id}")
    public ResponseEntity<MedicationDTO> getMedication(@PathVariable("medication-id") final Integer medicationId) {
        return ResponseEntity.ok(medicationsService.getMedication(medicationId));
    }

    @PostMapping(path = "/")
    public ResponseEntity<?> saveMedication(@RequestBody final MedicationDTO medication) {
        final MedicationDTO createdMedication = medicationsService.saveMedication(medication);

        if (createdMedication == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            final String resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                                                                    .path("/{medication-id}")
                                                                    .buildAndExpand(createdMedication.getId())
                                                                    .toUriString();
            return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, resourceLocation).build();
        }
    }

    @PatchMapping(path = "/{medication-id}", consumes = "application/json-patch+json")
    public ResponseEntity<?> patchMedication(@PathVariable(value = "medication-id") final Integer medicationId, @RequestBody final JsonPatch patch) {
        try {
            final MedicationDTO patchedMedication = medicationsService.patchMedication(medicationId, patch);

            if (patchedMedication == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

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
}
