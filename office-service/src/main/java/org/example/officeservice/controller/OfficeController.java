package org.example.officeservice.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.officeservice.dto.office.CreateOfficeRequest;
import org.example.officeservice.dto.office.OfficeResponse;
import org.example.officeservice.dto.office.UpdateOfficeRequest;
import org.example.officeservice.service.OfficeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/offices")
@RequiredArgsConstructor
public class OfficeController {

    private final OfficeService officeService;

    @PostMapping
    public ResponseEntity<OfficeResponse> createOffice(@Valid @RequestBody CreateOfficeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(officeService.createOffice(request));
    }

    @GetMapping("/{officeId}")
    public ResponseEntity<OfficeResponse> getOfficeById(@PathVariable UUID officeId) {
        return ResponseEntity.ok(officeService.getOfficeById(officeId));
    }

    @GetMapping
    public ResponseEntity<List<OfficeResponse>> getAllOffices() {
        return ResponseEntity.ok(officeService.getAllOffices());
    }

    @PutMapping("/{officeId}")
    public ResponseEntity<OfficeResponse> updateOffice(@PathVariable UUID officeId, @Valid @RequestBody UpdateOfficeRequest request) {
        return ResponseEntity.ok(officeService.updateOffice(officeId, request));
    }

    @DeleteMapping("/{officeId}")
    public ResponseEntity<Void> deleteOffice(@PathVariable UUID officeId) {
        officeService.deleteOffice(officeId);
        return ResponseEntity.noContent().build();
    }
}

