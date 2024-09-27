package we.cod.bnz.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanUpdateRequestDto {
    private Long id;
    private String title;
    private String color;
    private LocalDate date;
    private String startingHour;
    private String endingHour;
    private String summary;

}