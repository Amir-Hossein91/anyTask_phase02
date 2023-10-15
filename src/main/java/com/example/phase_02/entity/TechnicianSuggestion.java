package com.example.phase_02.entity;

import com.example.phase_02.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Entity
@SequenceGenerator(name = "id_generator", sequenceName = "technician_suggestion_sequence")
@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianSuggestion extends BaseEntity {
    @OneToOne
    private Technician technician;
    @ManyToOne
    private Order order;
    @Column(name = "Date_And_Time_Of_Tech_Suggestion")
    private LocalDateTime DateAndTimeOfTechSuggestion;
    @Range(min = 0, message = "Price can not be negative")
    @Column(name = "tech_Suggested_Price")
    private long techSuggestedPrice;
    @NotNull(message = "A technician suggested start date must be set")
    @Column(name = "tech_Suggested_Date")
    private LocalDateTime techSuggestedDate;
    @Range(min = 0, message = "Task duration can not be negative")
    @Column(name = "task_Estimated_Duration")
    private int taskEstimatedDuration;

    public String toString() {
        return "\n\t\t" + super.toString() +
                "\n\t\ttechnician = " + this.getTechnician().getId() +
                "\n\t\torder = " + this.getOrder().getId() +
                "\n\t\tDate_And_Time_Of_Tech_Suggestion = " + BaseEntity.getPersianDateTime(this.getDateAndTimeOfTechSuggestion()) +
                "\n\t\ttech_Suggested_Price = " + this.getTechSuggestedPrice() +
                "\n\t\ttech_Suggested_Date = " + BaseEntity.getPersianDateTime(this.getTechSuggestedDate()) +
                "\n\t\ttask_Estimated_Duration = " + this.getTaskEstimatedDuration() + "\n" ;
    }
}
