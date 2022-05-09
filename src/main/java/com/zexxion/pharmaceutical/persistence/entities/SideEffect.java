package com.zexxion.pharmaceutical.persistence.entities;

import com.zexxion.pharmaceutical.serialization.mappers.DomainEntity;
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

    @Column(name = "description", length = 48)
    private String description;

    @ManyToMany(mappedBy = "sideEffects")
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
}
