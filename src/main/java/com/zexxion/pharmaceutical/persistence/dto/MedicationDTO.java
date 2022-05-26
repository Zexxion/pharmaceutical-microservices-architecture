package com.zexxion.pharmaceutical.persistence.dto;

import com.zexxion.pharmaceutical.serialization.mapping.DomainDTO;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class MedicationDTO implements DomainDTO {
    private int id;
    private String name;
    private String description;
    private int dosage;
    private ProducerDTO producer;
    private List<SideEffectDTO> sideEffects;
    private Integer stock;
}
