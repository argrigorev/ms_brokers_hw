package ru.netology.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.netology.model.ApplicationStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDecisionEvent {
    private Long applicationId;
    private ApplicationStatus status;
}
