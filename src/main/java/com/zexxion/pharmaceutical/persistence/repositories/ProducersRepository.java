package com.zexxion.pharmaceutical.persistence.repositories;

import com.zexxion.pharmaceutical.persistence.entities.Producer;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ProducersRepository extends CrudRepository<Producer, Integer> {
    List<Producer> findAll();
}
