package com.zexxion.pharmaceutical.persistence.dto;

import com.zexxion.pharmaceutical.serialization.mapping.DomainDTO;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class MedicationDTO implements DomainDTO {
    private int id;
    private String name;
    private String description;
    private int dosage;
    private ProducerDTO producer;
    private List<SideEffectDTO> sideEffects;
    private Integer stock;
}
