package com.zexxion.pharmaceutical.configuration;

import com.zexxion.pharmaceutical.serialization.mappers.MedicationModelMapper;
import com.zexxion.pharmaceutical.serialization.mappers.ProducerModelMapper;
import com.zexxion.pharmaceutical.serialization.mappers.SideEffectModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {
    @Bean
    public MedicationModelMapper medicationModelMapper() {
        return new MedicationModelMapper();
    }

    @Bean
    public SideEffectModelMapper sideEffectModelMapper() {
        return new SideEffectModelMapper();
    }

    @Bean
    public ProducerModelMapper producerModelMapper() {
        return new ProducerModelMapper();
    }
}
