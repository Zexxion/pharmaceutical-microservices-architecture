package com.zexxion.pharmaceutical.persistence.repositories;

import com.zexxion.pharmaceutical.persistence.entities.SideEffect;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SideEffectsRepository extends CrudRepository<SideEffect, Integer> {
    List<SideEffect> findAll();
}
