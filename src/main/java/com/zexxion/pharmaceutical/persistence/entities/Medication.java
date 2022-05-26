package com.zexxion.pharmaceutical.persistence.entities;

import com.zexxion.pharmaceutical.serialization.mapping.DomainEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
public class Medication implements DomainEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "dosage", nullable = false)
    private Integer dosage;

    @ManyToOne
    @JoinColumn(name = "producer")
    private Producer producer;

    @ManyToMany(mappedBy = "medications")
    private List<SideEffect> sideEffects;

    @Transient
    private MedicationStock stock;

    public Medication(Integer id, String name, String description, Integer dosage, Producer producer, List<SideEffect> sideEffects, MedicationStock stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dosage = dosage;
        this.producer = producer;
        this.sideEffects = sideEffects;
        this.stock = stock;
    }

    public Medication(Integer id, String name, String description, Integer dosage, Producer producer, List<SideEffect> sideEffects) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dosage = dosage;
        this.producer = producer;
        this.sideEffects = sideEffects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medication that = (Medication) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(dosage, that.dosage) && Objects.equals(producer, that.producer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, dosage, producer);
    }

    @PreRemove
    public void beforeRemove() {
        this.sideEffects.clear();
    }
}
