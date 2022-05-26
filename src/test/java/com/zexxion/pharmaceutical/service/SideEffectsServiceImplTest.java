package com.zexxion.pharmaceutical.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.zexxion.pharmaceutical.persistence.dto.SideEffectDTO;
import com.zexxion.pharmaceutical.persistence.entities.SideEffect;
import com.zexxion.pharmaceutical.persistence.repositories.SideEffectsRepository;
import com.zexxion.pharmaceutical.serialization.mapping.SideEffectModelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class SideEffectsServiceImplTest {
    @InjectMocks
    private SideEffectModelMapper sideEffectModelMapper;

    private SideEffectsService sideEffectService;

    @Mock
    private SideEffectsRepository sideEffectsRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        sideEffectService = new SideEffectServiceImpl(sideEffectsRepository, sideEffectModelMapper);
    }

    @Test
    public void testGetSideEffects() {
        final List<SideEffect> sideEffectsEntity = getSideEffects();
        final List<SideEffectDTO> sideEffects = sideEffectsEntity.stream().map(sideEffectModelMapper::convertToDTO).collect(Collectors.toList());

        given(sideEffectsRepository.findAll()).willReturn(sideEffectsEntity);

        final List<SideEffectDTO> foundSideEffects = sideEffectService.getSideEffects();

        assertThat(sideEffects).isEqualTo(foundSideEffects);
    }

    @Test
    public void testGetSideEffect() {
        final SideEffect sideEffectEntity = getSideEffect();
        final SideEffectDTO sideEffect = sideEffectModelMapper.convertToDTO(sideEffectEntity);
        final Integer sideEffectId = sideEffect.getId();

        given(sideEffectsRepository.findById(sideEffectId)).willReturn(Optional.of(sideEffectEntity));

        final SideEffectDTO foundSideEffect = sideEffectService.getSideEffect(sideEffectId);

        verify(sideEffectsRepository).findById(sideEffectId);
        assertThat(sideEffect).isEqualTo(foundSideEffect);
    }

    @Test
    public void testSaveSideEffect() {
        final SideEffect sideEffectEntity = getSideEffect();
        final SideEffectDTO sideEffect = sideEffectModelMapper.convertToDTO(sideEffectEntity);
        given(sideEffectsRepository.save(sideEffectEntity)).willReturn(sideEffectEntity);

        final SideEffectDTO savedSideEffect = sideEffectService.saveSideEffect(sideEffect);

        verify(sideEffectsRepository).save(sideEffectEntity);
        assertThat(sideEffect).isEqualTo(savedSideEffect);
    }

    @Test
    public void testUpdateSideEffect() {
        final SideEffect sideEffectEntity = getSideEffect();
        final SideEffectDTO sideEffect = sideEffectModelMapper.convertToDTO(sideEffectEntity);

        given(sideEffectsRepository.save(sideEffectEntity)).willReturn(sideEffectEntity);

        final SideEffectDTO savedSideEffect = sideEffectService.saveSideEffect(sideEffect);

        verify(sideEffectsRepository).save(sideEffectEntity);
        assertThat(sideEffect).isEqualTo(savedSideEffect);
    }

    @Test
    public void testPatchSideEffect() throws IOException, JsonPatchException {
        final String patchString = "[{\"op\":\"replace\",\"path\":\"/description\",\"value\":\"AVC\"},{\"op\":\"remove\",\"path\":\"/description\"}]";
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode node = mapper.readTree(patchString);
        final JsonPatch patch = JsonPatch.fromJson(node);

        final SideEffect sideEffectEntity = getSideEffect();
        final SideEffectDTO sideEffect = sideEffectModelMapper.convertToDTO(sideEffectEntity);
        final SideEffectDTO patchedSideEffect = sideEffectModelMapper.applyPatchToDto(patch, sideEffect);
        final SideEffect patchedSideEffectEntity = sideEffectModelMapper.convertToEntity(patchedSideEffect);
        final Integer sideEffectId = sideEffectEntity.getId();

        given(sideEffectsRepository.findById(sideEffectId)).willReturn(Optional.of(sideEffectEntity));
        given(sideEffectsRepository.save(patchedSideEffectEntity)).willReturn(patchedSideEffectEntity);
        final SideEffectDTO savedSideEffect = sideEffectService.patchSideEffect(sideEffectId, patch);

        verify(sideEffectsRepository).save(patchedSideEffectEntity);
        assertThat(savedSideEffect).isEqualTo(patchedSideEffect);
    }

    @Test
    public void testDeleteSideEffect() {
        final Integer sideEffectId = 1;
        sideEffectService.deleteSideEffect(sideEffectId);
        verify(sideEffectsRepository).deleteById(sideEffectId);
    }

    private SideEffect getSideEffect() {
        return getSideEffects().get(0);
    }

    private List<SideEffect> getSideEffects() {
        return Arrays.asList(new SideEffect(1, "Maux de têtes"),
                      new SideEffect(2, "AVC"),
                      new SideEffect(3, "Diarrhée"));
    }
}
