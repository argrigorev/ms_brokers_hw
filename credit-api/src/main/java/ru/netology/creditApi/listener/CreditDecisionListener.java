package ru.netology.creditApi.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import ru.netology.creditApi.sevice.CreditApplicationService;
import ru.netology.creditApi.event.CreditDecisionEvent;

@RequiredArgsConstructor
public class CreditDecisionListener {
    private final CreditApplicationService service;

    @RabbitListener(queues = "credit-decisions")
    public void receiveDecision(CreditDecisionEvent decisionEvent) {
        service.updateStatus(decisionEvent.getApplicationId(), decisionEvent.getStatus());
    }
}
