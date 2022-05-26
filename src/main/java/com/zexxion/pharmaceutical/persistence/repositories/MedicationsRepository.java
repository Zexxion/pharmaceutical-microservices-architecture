package com.zexxion.pharmaceutical.persistence.repositories;

import com.zexxion.pharmaceutical.persistence.entities.Medication;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface MedicationsRepository extends CrudRepository<Medication, Integer> {
    List<Medication> findAll();
    List<Medication> findByIdIn(List<Integer> ids);
}
