package com.zexxion.pharmaceutical.persistence.entities;

import com.zexxion.pharmaceutical.serialization.mapping.DomainEntity;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
public class SideEffect implements DomainEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "description", length = 48, nullable = false)
    private String description;

    @ManyToMany
    @JoinTable(name = "medication_side_effect",
            joinColumns = { @JoinColumn(name = "id_side_effect") },
            inverseJoinColumns = { @JoinColumn(name = "id_medication") })
    private List<Medication> medications;

    public SideEffect() { }

    public SideEffect(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SideEffect that = (SideEffect) o;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description);
    }

    @PreRemove
    public void beforeRemove() {
        this.medications.forEach(medication -> medication.getSideEffects().remove(this));
    }
}
