package com.zexxion.pharmaceutical.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.zexxion.pharmaceutical.persistence.dto.ProducerDTO;
import com.zexxion.pharmaceutical.service.ProducersService;
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

    @GetMapping(path = "/")
    public ResponseEntity<List<ProducerDTO>> getProducers() {
        return ResponseEntity.ok(producersService.getProducers());
    }

    @GetMapping(path = "/{producer-id}")
    public ResponseEntity<ProducerDTO> getProducer(@PathVariable(name = "producer-id") final Integer producerId) {
        return ResponseEntity.ok(producersService.getProducer(producerId));
    }

    @PostMapping(path = "/")
    public ResponseEntity<?> saveProducer(@RequestBody final ProducerDTO producer) {
        final ProducerDTO createdProducer = producersService.saveProducer(producer);

        if (createdProducer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            final String resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                                                                       .path("/{producer-id}")
                                                                       .buildAndExpand(createdProducer.getId())
                                                                       .toUriString();

            return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, resourceLocation).build();
        }
    }

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


    @PutMapping(path = "/{producer-id}")
    public ResponseEntity<?> updateProducer(@PathVariable(name = "producer-id") final Integer producerId, @RequestBody ProducerDTO producer) {
        final ProducerDTO updatedProducer = producersService.updateProducer(producerId, producer);

        if (updatedProducer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
