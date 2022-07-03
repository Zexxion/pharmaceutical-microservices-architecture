package com.zexxion.pharmaceutical.serialization.mapping;

public interface DomainModelMapper {
    DomainDTO convertToDTO(final DomainEntity entity);
    DomainEntity convertToEntity(final DomainDTO dto);
}
