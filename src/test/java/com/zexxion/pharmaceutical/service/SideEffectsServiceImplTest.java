package com.zexxion.pharmaceutical.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
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
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public void Expect_GetSideEffectsList() {
        final List<SideEffect> sideEffectsEntity = getSideEffects();
        final List<SideEffectDTO> sideEffects = sideEffectsEntity.stream().map(sideEffectModelMapper::convertToDTO).collect(Collectors.toList());

        given(sideEffectsRepository.findAll()).willReturn(sideEffectsEntity);

        final List<SideEffectDTO> foundSideEffects = sideEffectService.getSideEffects();

        assertThat(sideEffects).isEqualTo(foundSideEffects);
    }

    @Test
    public void When_IdentifierIsValid_Expect_GetSideEffect() {
        final SideEffect sideEffectEntity = getSideEffect();
        final SideEffectDTO sideEffect = sideEffectModelMapper.convertToDTO(sideEffectEntity);
        final Integer sideEffectId = sideEffect.getId();

        given(sideEffectsRepository.findById(sideEffectId)).willReturn(Optional.of(sideEffectEntity));

        final SideEffectDTO foundSideEffect = sideEffectService.getSideEffect(sideEffectId);

        verify(sideEffectsRepository).findById(sideEffectId);
        assertThat(sideEffect).isEqualTo(foundSideEffect);
    }

    @Test
    public void When_IdentifierIsInvalid_Expect_GetNoneSideEffect() {
        final Integer sideEffectIdentifier = 1;

        given(sideEffectsRepository.findById(sideEffectIdentifier)).willReturn(Optional.empty());
        final SideEffectDTO nullSideEffect = sideEffectService.getSideEffect(sideEffectIdentifier);

        assertThat(nullSideEffect).isNull();
    }

    @Test
    public void When_IdentifierIsValid_Expect_SaveSideEffect() {
        final SideEffect sideEffectEntity = getSideEffect();
        final SideEffectDTO sideEffect = sideEffectModelMapper.convertToDTO(sideEffectEntity);
        given(sideEffectsRepository.save(sideEffectEntity)).willReturn(sideEffectEntity);

        final SideEffectDTO savedSideEffect = sideEffectService.saveSideEffect(sideEffect);

        verify(sideEffectsRepository).save(sideEffectEntity);
        assertThat(sideEffect).isEqualTo(savedSideEffect);
    }

    @Test
    public void Expect_UpdateSideEffect() {
        final SideEffect sideEffectEntity = getSideEffect();
        final SideEffectDTO sideEffect = sideEffectModelMapper.convertToDTO(sideEffectEntity);

        given(sideEffectsRepository.save(sideEffectEntity)).willReturn(sideEffectEntity);

        final SideEffectDTO updatedSideEffect = sideEffectService.updateSideEffect(sideEffect.getId(), sideEffect);

        verify(sideEffectsRepository).save(sideEffectEntity);
        assertThat(sideEffect).isEqualTo(updatedSideEffect);
    }

    @Test
    public void When_IdentifierIsValid_Expect_PatchSideEffect() throws IOException, JsonPatchException {
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
    public void When_IdentifierIsInvalid_Expect_PatchSideEffectToFail_ThrowResourceNotFoundException() {
        final Integer sideEffectId = 122;

        given(sideEffectsRepository.findById(sideEffectId)).willReturn(Optional.empty());

        final ResourceNotFoundException expectedException = assertThrows(ResourceNotFoundException.class, () -> sideEffectService.patchSideEffect(sideEffectId, null), "ResourceNotFoundException was expected");
        assertThat(expectedException.getMessage().toLowerCase()).isEqualTo(String.format("Could not found a side effect with id %d", sideEffectId).toLowerCase());
    }

    @Test
    public void When_JsonPatchIsInvalid_Expect_PatchSideEffectToFail_ThrowInvalidTypeIdException() {
        final String invalidPatchString = "[{\"op\":\"replace\",\"path\":\"/invalidField\",\"value\":\"AVC\"},{\"ope\":\"remove\",\"path\":\"/invalidField2\"}]";
        final ObjectMapper mapper = new ObjectMapper();

        assertThrows(InvalidTypeIdException.class, () -> {
            final JsonNode node = mapper.readTree(invalidPatchString);
            final JsonPatch patch = JsonPatch.fromJson(node);
            final Integer sideEffectId = 122;

            sideEffectService.patchSideEffect(sideEffectId, patch);
        }, "InvalidTypeIdException was expected");
    }

    @Test
    public void Expect_DeleteSideEffect() {
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
