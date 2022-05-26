package com.zexxion.pharmaceutical.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.zexxion.pharmaceutical.persistence.dto.ProducerDTO;
import com.zexxion.pharmaceutical.persistence.entities.Producer;
import com.zexxion.pharmaceutical.persistence.repositories.ProducersRepository;
import com.zexxion.pharmaceutical.serialization.mapping.ProducerModelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class ProducersServiceImplTest {
    private ProducersService producersService;

    @InjectMocks
    private ProducerModelMapper producerModelMapper;

    @Mock
    private ProducersRepository producersRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        producersService = new ProducersServiceImpl(producersRepository, producerModelMapper);
    }

    @Test
    public void testGetProducersList() {
        final List<Producer> producers = getProducers();
        given(producersRepository.findAll()).willReturn(producers);

        final List<Producer> producersTest = producersService.getProducers().stream()
                                                                            .map(producerModelMapper::convertToEntity)
                                                                            .collect(Collectors.toList());

        assertThat(producersTest).isEqualTo(producers);
    }

    @Test
    public void testGetProducer() {
        final Producer producerEntity = getProducer();
        final ProducerDTO producer = producerModelMapper.convertToDTO(producerEntity);
        final Integer producerId = producerEntity.getId();

        given(producersRepository.findById(producerId)).willReturn(Optional.of(producerEntity));
        final ProducerDTO foundProducer = producersService.getProducer(producerId);

        verify(producersRepository).findById(producerId);
        assertThat(producer).isEqualTo(foundProducer);
    }

    @Test
    public void testSaveProducer() {
        final Producer producerTest = getProducers().get(0);
        final ProducerDTO producerTestDto = producerModelMapper.convertToDTO(producerTest);

        given(producersRepository.save(producerTest)).willReturn(producerTest);

        final ProducerDTO savedProducer = producersService.saveProducer(producerTestDto);

        verify(producersRepository).save(producerTest);
        assertThat(producerTestDto).isEqualTo(savedProducer);
    }

    @Test
    public void testUpdateProducer() {
        final Producer producerEntity = getProducer();
        final ProducerDTO producer = producerModelMapper.convertToDTO(producerEntity);
        final Integer producerId = producerEntity.getId();

        given(producersRepository.save(producerEntity)).willReturn(producerEntity);
        final ProducerDTO updatedProducer = producersService.updateProducer(producerId, producer);

        verify(producersRepository).save(producerEntity);
        assertThat(producer).isEqualTo(updatedProducer);
    }

    @Test
    public void testPatchProducer() throws IOException, JsonPatchException {
        final String patchString = "[{\"op\":\"replace\",\"path\":\"/name\",\"value\":\"Pfizer Inc. - patched\"}]";
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode node = mapper.readTree(patchString);
        final JsonPatch patch = JsonPatch.fromJson(node);

        final Producer producerEntity = getProducer();
        final ProducerDTO producer = producerModelMapper.convertToDTO(producerEntity);
        final ProducerDTO patchedProducer = producerModelMapper.applyPatchToDto(patch, producer);
        final Producer patchedProducerEntity = producerModelMapper.convertToEntity(patchedProducer);
        final Integer producerId = producerEntity.getId();

        given(producersRepository.findById(producerId)).willReturn(Optional.of(producerEntity));
        given(producersRepository.save(patchedProducerEntity)).willReturn(patchedProducerEntity);
        final ProducerDTO savedPatchedProducer = producersService.patchProducer(producerId, patch);

        verify(producersRepository).save(patchedProducerEntity);
        assertThat(savedPatchedProducer).isEqualTo(patchedProducer);
    }

    @Test
    public void testDeleteProducer() {
        final int producerId = 1;
        producersService.deleteProducer(producerId);
        verify(producersRepository).deleteById(producerId);
    }

    private Producer getProducer() {
        return getProducers().get(0);
    }

    private List<Producer> getProducers() {
        return Collections.singletonList(new Producer(1, "Pfizer inc.", "Brussels", "Belgium"));
    }
}
