package ru.netology.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.netology.event.CreditApplicationEvent;
import ru.netology.event.CreditDecisionEvent;
import ru.netology.model.ApplicationStatus;
import ru.netology.config.RabbitConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CreditDecisionService {
    private final RabbitTemplate rabbitTemplate;

    public CreditDecisionService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void processCreditApplication(CreditApplicationEvent event) {
        // Ежемесячный платёж = сумма кредита / срок
        BigDecimal monthlyPayment = event.getAmount()
                .divide(BigDecimal.valueOf(event.getTerm()), RoundingMode.HALF_UP);

        // Максимально допустимый платёж = 50% дохода
        BigDecimal maxAllowedPayment = event.getIncome()
                .multiply(BigDecimal.valueOf(0.5));

        // Проверка, платёж + текущая нагрузка <= 50% дохода
        ApplicationStatus status = monthlyPayment.add(event.getCurrentCreditLoad())
                .compareTo(maxAllowedPayment) <= 0
                ? ApplicationStatus.APPROVED
                : ApplicationStatus.REJECTED;

        // Событие с решением
        CreditDecisionEvent decisionEvent = new CreditDecisionEvent(event.getId(), status);

        // Отправляем результат обратно в очередь credit-decision
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_NAME, decisionEvent);

        System.out.println("Решение по кредиту отправлено по заявке " + event.getId() + ": " + status);
    }
}
