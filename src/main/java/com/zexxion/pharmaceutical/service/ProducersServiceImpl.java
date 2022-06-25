package com.zexxion.pharmaceutical.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.zexxion.pharmaceutical.persistence.dto.ProducerDTO;
import com.zexxion.pharmaceutical.persistence.entities.Producer;
import com.zexxion.pharmaceutical.persistence.repositories.ProducersRepository;
import com.zexxion.pharmaceutical.serialization.mapping.ProducerModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProducersServiceImpl implements ProducersService {
    private final ProducersRepository producersRepository;
    private final ProducerModelMapper modelMapper;

    @Override
    public List<ProducerDTO> getProducers() {
        return producersRepository.findAll()
                                  .stream()
                                  .map(modelMapper::convertToDTO)
                                  .collect(Collectors.toList());
    }

    @Override
    public ProducerDTO getProducer(final Integer producerId) {
        Optional<Producer> producerOptional = producersRepository.findById(producerId);

        if (producerOptional.isPresent()) {
            final Optional<ProducerDTO> producerDtoOptional = producerOptional.map(modelMapper::convertToDTO);

            return producerDtoOptional.orElse(null);
        } else {
            return null;
        }
    }

    @Override
    public ProducerDTO saveProducer(final ProducerDTO producer) {
        final Producer producerEntity = modelMapper.convertToEntity(producer);

        return modelMapper.convertToDTO(producersRepository.save(producerEntity));
    }

    @Override
    public ProducerDTO updateProducer(final Integer producerId, final ProducerDTO producer) {
        producer.setId(producerId);
        final Producer producerEntity = modelMapper.convertToEntity(producer);

        return modelMapper.convertToDTO(producersRepository.save(producerEntity));
    }

    @Override
    public ProducerDTO patchProducer(final Integer producerId, final JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        final Optional<Producer> optionalProducer = producersRepository.findById(producerId);

        if (optionalProducer.isPresent()) {
            final ProducerDTO producerDto = modelMapper.convertToDTO(optionalProducer.get());
            final ProducerDTO patchedProducer = modelMapper.applyPatchToDto(patch, producerDto);
            final Producer producerEntity = modelMapper.convertToEntity(patchedProducer);

            return modelMapper.convertToDTO(producersRepository.save(producerEntity));
        } else {
            throw new ResourceNotFoundException(String.format("Could not found a producer with id %d", producerId));
        }
    }

    @Override
    public void deleteProducer(final Integer producerId) {
        producersRepository.deleteById(producerId);
    }
}
