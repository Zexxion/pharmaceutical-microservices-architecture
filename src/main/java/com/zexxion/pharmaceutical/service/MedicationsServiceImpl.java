package com.zexxion.pharmaceutical.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.zexxion.pharmaceutical.persistence.dto.MedicationDTO;
import com.zexxion.pharmaceutical.persistence.entities.Medication;
import com.zexxion.pharmaceutical.persistence.entities.MedicationStock;
import com.zexxion.pharmaceutical.persistence.repositories.MedicationsRepository;
import com.zexxion.pharmaceutical.persistence.repositories.MedicationsStockRepository;
import com.zexxion.pharmaceutical.serialization.mapping.MedicationModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MedicationsServiceImpl implements MedicationsService {
    private final MedicationsRepository medicationsRepository;
    private final MedicationsStockRepository medicationsStockRepository;
    private final MedicationModelMapper modelMapper;

    @Override
    public List<MedicationDTO> getMedications() {
        final List<Medication> medicationsEntities = medicationsRepository.findAll();
        medicationsEntities.forEach(medication -> medicationsStockRepository.findByMedicationId(medication.getId()).ifPresent(medication::setStock));

        return medicationsEntities.stream()
                                  .map(modelMapper::convertToDTO)
                                  .collect(Collectors.toList());
    }

    @Override
    public MedicationDTO getMedication(final Integer medicationId) {
        final Optional<Medication> medicationOptional = medicationsRepository.findById(medicationId);

        if (medicationOptional.isPresent()) {
            medicationsStockRepository.findByMedicationId(medicationId).ifPresent(medicationOptional.get()::setStock);

            return modelMapper.convertToDTO(medicationOptional.get());
        } else {
            return null;
        }
    }

    @Override
    public MedicationDTO saveMedication(final MedicationDTO medication) {
        final Medication medicationEntity = modelMapper.convertToEntity(medication);
        final Medication persistedMedication = medicationsRepository.save(medicationEntity);
        final MedicationStock stock = medicationsStockRepository.save(new MedicationStock(persistedMedication, medication.getStock()));
        medicationsStockRepository.save(stock);

        return modelMapper.convertToDTO(persistedMedication);
    }

    @Override
    public MedicationDTO updateMedication(final Integer medicationId, final MedicationDTO medication) {
        medication.setId(medicationId);
        final Medication medicationEntity = modelMapper.convertToEntity(medication);
        final Integer medicationStock = medication.getStock();

        if (medicationStock != null) {
            medicationsStockRepository.updateStock(medicationStock, medicationId);
        }

        final Medication updatedEntity = medicationsRepository.save(medicationEntity);
        return modelMapper.convertToDTO(updatedEntity);
    }

    @Override
    public MedicationDTO patchMedication(final Integer medicationId, final JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        final Optional<Medication> optionalMedication = medicationsRepository.findById(medicationId);

        if (optionalMedication.isPresent()) {
            final MedicationModelMapper mapper = new MedicationModelMapper();
            final MedicationDTO medicationDto = mapper.convertToDTO(optionalMedication.get());
            final MedicationDTO patchedMedication = mapper.applyPatchToDto(patch, medicationDto);
            final Medication medicationEntity = mapper.convertToEntity(patchedMedication);

            if (patchedMedication.getStock() != null) {
                medicationsStockRepository.updateStock(patchedMedication.getStock(), medicationId);
            }

            return mapper.convertToDTO(medicationsRepository.save(medicationEntity));
        } else {
            throw new ResourceNotFoundException(String.format("Could not found a medication with id %d", medicationId));
        }
    }

    @Override
    public void deleteMedication(Integer medicationId) {
        medicationsStockRepository.deleteByMedicationId(medicationId);
        medicationsRepository.deleteById(medicationId);
    }
}
