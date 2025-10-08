package ru.netology.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import ru.netology.config.RabbitConfig;
import ru.netology.event.CreditApplicationEvent;
import ru.netology.event.CreditDecisionEvent;
import ru.netology.model.ApplicationStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
public class CreditApplicationListener {
    private final RabbitTemplate rabbitTemplate;

    @KafkaListener(topics = "credit-applications", groupId = "credit-processor-group")
    public void handleCreditApplication(CreditApplicationEvent event) {

        // Расчёт ежемесячного платежа по кредиту
        BigDecimal monthlyPayment = event.getAmount()
                .divide(BigDecimal.valueOf(event.getTerm()), RoundingMode.HALF_UP);

        // Проверка: платежи не должны превышать 50% дохода
        BigDecimal maxAllowedPayment = event.getIncome().multiply(BigDecimal.valueOf(0.5));

        ApplicationStatus status = monthlyPayment.add(event.getCurrentCreditLoad())
                .compareTo(maxAllowedPayment) <= 0
                ? ApplicationStatus.APPROVED
                : ApplicationStatus.REJECTED;

        // Создаём событие с решением
        CreditDecisionEvent decisionEvent = new CreditDecisionEvent(event.getId(), status);

        // Отправляем результат обратно в очередь RabbitMQ
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_NAME, decisionEvent);

        System.out.println("Решение по заявке ID=" + event.getId() + ": " + status);
    }


}
