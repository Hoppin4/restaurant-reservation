package org.example.officeservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.officeservice.dto.company.CompanyResponse;
import org.example.officeservice.dto.company.CreateCompanyRequest;
import org.example.officeservice.dto.company.UpdateCompanyRequest;
import org.example.officeservice.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@Valid @RequestBody CreateCompanyRequest request) {
        CompanyResponse response = companyService.createCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable UUID companyId) {
        CompanyResponse response = companyService.getCompanyById(companyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        List<CompanyResponse> response = companyService.getAllCompanies();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable UUID companyId, @Valid @RequestBody UpdateCompanyRequest request) {
        CompanyResponse response = companyService.updateCompany(companyId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable UUID companyId) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.noContent().build();
    }
}
