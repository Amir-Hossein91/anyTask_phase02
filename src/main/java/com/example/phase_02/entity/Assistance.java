package com.example.phase_02.entity;

import com.example.phase_02.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;


@Entity
@SequenceGenerator(name = "id_generator", sequenceName = "assistance_sequence")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Component
public class Assistance extends BaseEntity {
    @NotNull(message = "Assistance title can not be null")
    private String title;

    public String toString() {
        return  this.getTitle() ;
    }
}
