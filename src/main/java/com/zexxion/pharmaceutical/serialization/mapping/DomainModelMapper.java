package com.zexxion.pharmaceutical.serialization.mapping;

//TODO: Make this interface a Spring Bean in order to allow it to be dependency injected
public interface DomainModelMapper {
    DomainDTO convertToDTO(DomainEntity entity);
    DomainEntity convertToEntity(DomainDTO dto);
}
