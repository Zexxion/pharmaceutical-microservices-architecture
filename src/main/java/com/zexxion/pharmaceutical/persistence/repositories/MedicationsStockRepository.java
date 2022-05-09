package com.zexxion.pharmaceutical.persistence.repositories;

import com.zexxion.pharmaceutical.persistence.entities.MedicationStock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MedicationsStockRepository extends CrudRepository<MedicationStock, Integer> {
    @Query("SELECT m FROM MedicationStock m WHERE id_medication = :medicationId")
    Optional<MedicationStock> findByMedicationId(@Param("medicationId") final Integer medicationId);

    @Transactional
    @Modifying
    @Query("UPDATE MedicationStock m SET m.stock = :stock WHERE m.medication.id = :medicationId")
    void updateStock(@Param("stock") final Integer stock, @Param("medicationId") final Integer medicationId);
}
