package ru.netology.creditProcessor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.netology.creditApi.event.CreditApplicationEvent;
import ru.netology.creditApi.event.CreditDecisionEvent;
import ru.netology.creditApi.model.ApplicationStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CreditProcessorService {

    private final RabbitTemplate rabbitTemplate;

    @KafkaListener(topics = "credit-applications")
    public void handleApplication(CreditApplicationEvent event) {
        System.out.println("Получена заявка на кредит: " + event.getId());
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
        rabbitTemplate.convertAndSend("credit-decisions", decisionEvent);

        System.out.println("Решение по кредиту отправлено по заявке " + event.getId() + ": " + status);
    }
}
