package ru.netology.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import ru.netology.config.RabbitConfig;
import ru.netology.event.CreditDecisionEvent;
import ru.netology.sevice.CreditApplicationService;

@RequiredArgsConstructor
public class CreditDecisionListener {
    private final CreditApplicationService service;

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receiveDecision(CreditDecisionEvent decisionEvent) {
        service.updateStatus(decisionEvent.getApplicationId(), decisionEvent.getStatus());
    }
}
