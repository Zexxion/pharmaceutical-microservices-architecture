package com.zexxion.pharmaceutical.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.zexxion.pharmaceutical.persistence.dto.SideEffectDTO;
import com.zexxion.pharmaceutical.persistence.entities.SideEffect;
import com.zexxion.pharmaceutical.persistence.repositories.SideEffectsRepository;
import com.zexxion.pharmaceutical.serialization.mapping.SideEffectModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SideEffectServiceImpl implements SideEffectsService {
    private final SideEffectsRepository sideEffectsRepository;
    private final SideEffectModelMapper modelMapper;

    @Override
    public List<SideEffectDTO> getSideEffects() {
        final List<SideEffect> sideEffects = sideEffectsRepository.findAll();

        return sideEffects.stream()
                .map(modelMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SideEffectDTO getSideEffect(final Integer sideEffectId) {
        final Optional<SideEffect> sideEffectEntity = sideEffectsRepository.findById(sideEffectId);

        return sideEffectEntity.map(modelMapper::convertToDTO).orElse(null);
    }

    @Override
    public SideEffectDTO saveSideEffect(final SideEffectDTO sideEffect) {
        final SideEffect sideEffectEntity = modelMapper.convertToEntity(sideEffect);
        final SideEffect persistedSideEffect = sideEffectsRepository.save(sideEffectEntity);

        return modelMapper.convertToDTO(persistedSideEffect);
    }

    @Override
    public SideEffectDTO updateSideEffect(final Integer sideEffectId, final SideEffectDTO sideEffect) {
        sideEffect.setId(sideEffectId);
        final SideEffect sideEffectEntity = modelMapper.convertToEntity(sideEffect);

        return modelMapper.convertToDTO(sideEffectsRepository.save(sideEffectEntity));
    }

    @Override
    public SideEffectDTO patchSideEffect(final Integer sideEffectId, final JsonPatch patch) throws JsonPatchException, JsonProcessingException, ResourceNotFoundException {
        final Optional<SideEffect> optionalSideEffect = sideEffectsRepository.findById(sideEffectId);

        if (optionalSideEffect.isPresent()) {
            final SideEffectDTO sideEffectDto = modelMapper.convertToDTO(optionalSideEffect.get());
            final SideEffectDTO patchedSideEffect = modelMapper.applyPatchToDto(patch, sideEffectDto);
            final SideEffect sideEffectEntity = modelMapper.convertToEntity(patchedSideEffect);

            return modelMapper.convertToDTO(sideEffectsRepository.save(sideEffectEntity));
        } else {
            throw new ResourceNotFoundException(String.format("Could not found a side effect with id %d", sideEffectId));
        }
    }

    @Override
    public void deleteSideEffect(final Integer sideEffectId) {
        sideEffectsRepository.deleteById(sideEffectId);
    }
}
