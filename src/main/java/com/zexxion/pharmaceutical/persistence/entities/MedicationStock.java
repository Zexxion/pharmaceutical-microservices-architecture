package com.zexxion.pharmaceutical.persistence.entities;

import com.zexxion.pharmaceutical.serialization.mapping.DomainEntity;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
public class MedicationStock implements DomainEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_medication")
    private Medication medication;

    @Column(name = "stock")
    private Integer stock;

    public MedicationStock() { }

    public MedicationStock(Integer stock) {
        this.stock = stock;
    }

    public MedicationStock(final Medication medication, Integer stock) {
        this.medication = medication;
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationStock that = (MedicationStock) o;
        return Objects.equals(medication, that.medication) && Objects.equals(stock, that.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medication, stock);
    }
}
