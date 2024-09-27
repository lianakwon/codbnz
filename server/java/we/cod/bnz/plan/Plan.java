package we.cod.bnz.plan;

import jakarta.persistence.*;
import lombok.*;
import we.cod.bnz.account.Account;
import we.cod.bnz.plan.dto.PlanUpdateRequestDto;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String color;
    private LocalDate date;
    private String startingHour;
    private String endingHour;
    private String summary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    public Account account;

    @Builder
    public Plan(String title, String color, LocalDate date, String startingHour, String endingHour, String summary) {
        this.title = title;
        this.color = color;
        this.date = date;
        this.startingHour = startingHour;
        this.endingHour = endingHour;
        this.summary = summary;
    }

    //== Member & Event 연관관계 편의 메소드 ==//
    public void setMappingMember(Account account) {
        this.account = account;
//        account.getPlan().add(this);
    }

    public void updateEvent(PlanUpdateRequestDto request) {
        this.id = request.getId();
        this.title = request.getTitle();
        this.color = request.getColor();
        this.date = request.getDate();
        this.startingHour = request.getStartingHour();
        this.endingHour = request.getEndingHour();
        this.summary = request.getSummary();
    }
}