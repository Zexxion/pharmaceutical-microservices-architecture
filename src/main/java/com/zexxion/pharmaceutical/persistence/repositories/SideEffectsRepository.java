package com.zexxion.pharmaceutical.persistence.repositories;

import com.zexxion.pharmaceutical.persistence.entities.SideEffect;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SideEffectsRepository extends CrudRepository<SideEffect, Integer> {
    List<SideEffect> findAll();
}
