package org.example.officeservice.service;

import org.example.officeservice.dto.company.CompanyResponse;
import org.example.officeservice.dto.company.CreateCompanyRequest;
import org.example.officeservice.dto.company.UpdateCompanyRequest;
import org.example.officeservice.entity.Company;
import org.example.officeservice.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public CompanyResponse createCompany(CreateCompanyRequest request) {

        Company company = Company.builder()
                .name(request.getName())
                .taxNumber(request.getTaxNumber())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();

        companyRepository.save(company);

        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .taxNumber(company.getTaxNumber())
                .email(company.getEmail())
                .phone(company.getPhone())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }

    public CompanyResponse getCompanyById(UUID companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Company not found"));
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .taxNumber(company.getTaxNumber())
                .email(company.getEmail())
                .phone(company.getPhone())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }


    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(company -> CompanyResponse.builder()
                        .id(company.getId())
                        .name(company.getName())
                        .taxNumber(company.getTaxNumber())
                        .email(company.getEmail())
                        .phone(company.getPhone())
                        .createdAt(company.getCreatedAt())
                        .updatedAt(company.getUpdatedAt())
                        .build())
                .toList();
    }

    public CompanyResponse updateCompany(UUID companyId, UpdateCompanyRequest request) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Company not found"));

        company.setName(request.getName());
        company.setTaxNumber(request.getTaxNumber());
        company.setEmail(request.getEmail());
        company.setPhone(request.getPhone());

        companyRepository.save(company);

        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .taxNumber(company.getTaxNumber())
                .email(company.getEmail())
                .phone(company.getPhone())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }

    public void deleteCompany(UUID companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Company not found"));
        companyRepository.delete(company);
    }
}
