package com.zexxion.pharmaceutical.persistence.dto;

import com.zexxion.pharmaceutical.serialization.mapping.DomainDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProducerDTO implements DomainDTO {
    private int id;
    private String name;
    private String locality;
    private String country;

    public ProducerDTO() { }

    public ProducerDTO(int id) {
        this.id = id;
    }
}
