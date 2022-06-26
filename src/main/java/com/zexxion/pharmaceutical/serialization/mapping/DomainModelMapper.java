package com.zexxion.pharmaceutical.serialization.mapping;

public interface DomainModelMapper {
    DomainDTO convertToDTO(DomainEntity entity);
    DomainEntity convertToEntity(DomainDTO dto);
}
