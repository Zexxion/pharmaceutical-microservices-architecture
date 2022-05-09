package com.zexxion.pharmaceutical.persistence.dto;

import com.zexxion.pharmaceutical.serialization.mappers.DomainDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SideEffectDTO implements DomainDTO {
    private int id;
    private String description;
}
