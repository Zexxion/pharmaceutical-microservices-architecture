package com.zexxion.pharmaceutical.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zexxion.pharmaceutical.serialization.mappers.DomainEntity;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Producer implements DomainEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 48, unique = true)
    private String name;

    @Column(name = "locality", nullable = false, length = 48)
    private String locality;

    @Column(name = "country", length = 36)
    private String country;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "producer")
    private List<Medication> medications;

    public Producer() { }

    public Producer(Integer id, String name, String locality, String country) {
        this.id = id;
        this.name = name;
        this.locality = locality;
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producer producer = (Producer) o;
        return Objects.equals(id, producer.id) && Objects.equals(name, producer.name) && Objects.equals(locality, producer.locality) && Objects.equals(country, producer.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, locality, country);
    }
}
