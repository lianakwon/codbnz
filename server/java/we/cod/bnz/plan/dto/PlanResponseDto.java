package we.cod.bnz.plan.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import we.cod.bnz.plan.Plan;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PlanResponseDto {
    private Long id;
    private String title;
    private String color;
    private LocalDate date;
    private String startingHour;
    private String endingHour;
    private String summary;

    @Builder
    public PlanResponseDto(Plan plan) {
        this.id = plan.getId();
        this.title = plan.getTitle();
        this.color = plan.getColor();
        this.date = plan.getDate();
        this.startingHour = plan.getStartingHour();
        this.endingHour = plan.getEndingHour();
        this.summary = plan.getSummary();
    }


}