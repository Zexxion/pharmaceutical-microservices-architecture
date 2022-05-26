package com.zexxion.pharmaceutical.service;

import com.zexxion.pharmaceutical.persistence.dto.SideEffectDTO;
import com.zexxion.pharmaceutical.persistence.entities.SideEffect;
import com.zexxion.pharmaceutical.persistence.repositories.SideEffectsRepository;
import com.zexxion.pharmaceutical.serialization.mapping.SideEffectModelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
    public void testSaveSideEffect() {
        final SideEffectDTO sideEffect = getSideEffectDto();
        final SideEffect sideEffectEntity = sideEffectModelMapper.convertToEntity(sideEffect);
        given(sideEffectsRepository.save(sideEffectEntity)).willReturn(sideEffectEntity);

        final SideEffectDTO savedSideEffect = sideEffectService.saveSideEffect(sideEffect);

        verify(sideEffectsRepository).save(sideEffectEntity);
        assertThat(sideEffect).isEqualTo(savedSideEffect);
    }

    @Test
    public void testUpdateSideEffect() {
        final SideEffectDTO sideEffect = getSideEffectDto();
        final SideEffect sideEffectEntity = sideEffectModelMapper.convertToEntity(sideEffect);

        given(sideEffectsRepository.save(sideEffectEntity)).willReturn(sideEffectEntity);

        final SideEffectDTO savedSideEffect = sideEffectService.saveSideEffect(sideEffect);

        verify(sideEffectsRepository).save(sideEffectEntity);
        assertThat(sideEffect).isEqualTo(savedSideEffect);
    }

    @Test
    public void testDeleteSideEffect() {
        final Integer sideEffectId = 1;
        sideEffectService.deleteSideEffect(sideEffectId);
        verify(sideEffectsRepository).deleteById(sideEffectId);
    }

    private SideEffectDTO getSideEffectDto() {
        return new SideEffectDTO(1, "Maux de têtes");
    }
}
