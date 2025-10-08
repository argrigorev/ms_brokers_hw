package ru.netology.creditApi.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.netology.creditApi.model.ApplicationStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDecisionEvent {
    private Long applicationId;
    private ApplicationStatus status;
}
