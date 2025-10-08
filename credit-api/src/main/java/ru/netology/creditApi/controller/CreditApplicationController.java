package ru.netology.creditApi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.netology.creditApi.dto.CreditApplicationResponse;
import ru.netology.creditApi.model.ApplicationStatus;
import ru.netology.creditApi.sevice.CreditApplicationService;
import ru.netology.creditApi.dto.CreditApplicationRequest;

@RestController
@RequestMapping("/api/credit-applications")
@RequiredArgsConstructor
public class CreditApplicationController {
    private final CreditApplicationService service;

    @PostMapping
    public CreditApplicationResponse createApplication(@RequestBody CreditApplicationRequest request) {
        Long id = service.createApplication(request);
        return new CreditApplicationResponse(id);
    }

    @GetMapping("/{id}/status")
    public ApplicationStatus getStatus(@PathVariable Long id) {
        return service.getApplicationStatus(id);
    }
}
