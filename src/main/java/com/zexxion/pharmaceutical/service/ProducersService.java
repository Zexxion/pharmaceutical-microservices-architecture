package com.zexxion.pharmaceutical.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.zexxion.pharmaceutical.persistence.dto.ProducerDTO;

import java.util.List;

public interface ProducersService {
    List<ProducerDTO> getProducers();
    ProducerDTO getProducer(final Integer producerId);
    ProducerDTO saveProducer(final ProducerDTO producer);
    ProducerDTO updateProducer(final Integer producerId, final ProducerDTO producer);
    ProducerDTO patchProducer(final Integer producerId, final JsonPatch patch) throws JsonPatchException, JsonProcessingException;
}
