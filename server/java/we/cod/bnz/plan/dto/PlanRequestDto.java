package we.cod.bnz.plan.dto;

import lombok.*;
import we.cod.bnz.plan.Plan;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanRequestDto {
    private String title;
    private String color;
    private LocalDate date;
    private String startingHour;
    private String endingHour;
    private String summary;

    @Builder
    public static Plan ofEntity(PlanRequestDto request) {
        return Plan.builder()
                .title(request.title)
                .color(request.color)
                .date(request.date)
                .startingHour(request.startingHour)
                .endingHour(request.endingHour)
                .summary(request.summary)
                .build();
    }
}