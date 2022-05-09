package com.zexxion.pharmaceutical.service;

import com.zexxion.pharmaceutical.persistence.dto.ProducerDTO;
import com.zexxion.pharmaceutical.persistence.entities.Producer;
import com.zexxion.pharmaceutical.persistence.repositories.ProducersRepository;
import com.zexxion.pharmaceutical.serialization.mapping.ProducerModelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class ProducersServiceImplTest {
    private ProducersService producersService;
    private ProducerModelMapper producerModelMapper;

    @Mock
    private ProducersRepository producersRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        producersService = new ProducersServiceImpl(producersRepository, producerModelMapper);
        producerModelMapper = new ProducerModelMapper();
    }

    @Test
    public void testGetProducersList() {
        final List<Producer> producers = getGeneratedProducers();
        given(producersRepository.findAll()).willReturn(producers);

        final List<Producer> producersTest = producersService.getProducers().stream()
                                                                            .map(producerModelMapper::convertToEntity)
                                                                            .collect(Collectors.toList());

        assertThat(producersTest).isEqualTo(producers);
    }

    @Test
    public void testSaveProducer() {
        final Producer producerTest = getGeneratedProducers().get(0);
        final ProducerDTO producerTestDto = producerModelMapper.convertToDTO(producerTest);

        given(producersRepository.save(producerTest)).willReturn(producerTest);
        given(producersService.saveProducer(producerTestDto)).willReturn(producerTestDto);
        verify(producersRepository).save(producerTest);
    }

    private List<Producer> getGeneratedProducers() {
        return Collections.singletonList(new Producer(1, "Pfizer inc.", "Brussels", "Belgium"));
    }
}
