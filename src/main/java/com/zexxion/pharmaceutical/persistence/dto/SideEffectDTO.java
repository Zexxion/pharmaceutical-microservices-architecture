package com.zexxion.pharmaceutical.persistence.dto;

import com.zexxion.pharmaceutical.serialization.mapping.DomainDTO;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class SideEffectDTO implements DomainDTO {
    private int id;
    private String description;
}
