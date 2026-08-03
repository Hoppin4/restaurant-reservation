package org.example.officeservice.service;

import org.example.officeservice.dto.office.CreateOfficeRequest;
import org.example.officeservice.dto.office.OfficeResponse;
import org.example.officeservice.dto.office.UpdateOfficeRequest;
import org.example.officeservice.entity.Company;
import org.example.officeservice.entity.Office;
import org.example.officeservice.repository.CompanyRepository;
import org.example.officeservice.repository.OfficeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OfficeService {
    private final OfficeRepository officeRepository;
    private final CompanyRepository companyRepository;

    public OfficeService(OfficeRepository officeRepository, CompanyRepository companyRepository) {
        this.officeRepository = officeRepository;
        this.companyRepository = companyRepository;
    }

    public OfficeResponse createOffice(CreateOfficeRequest request) {

        Company company = companyRepository.findById(request.getCompanyId()).orElseThrow(() -> new RuntimeException("Company not found"));

        Office office = Office.builder()
                .company(company)
                .name(request.getName())
                .country(request.getCountry())
                .city(request.getCity())
                .district(request.getDistrict())
                .address(request.getAddress())
                .timezone(request.getTimezone())
                .build();

        officeRepository.save(office);

        return mapToResponse(office);
    }

    public OfficeResponse getOfficeById(UUID officeId) {
        Office office = officeRepository.findById(officeId).orElseThrow(() -> new RuntimeException("Office not found"));
        return mapToResponse(office);
    }

    public List<OfficeResponse> getAllOffices() {
        return officeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OfficeResponse updateOffice(UUID officeId, UpdateOfficeRequest request) {
        Office office = officeRepository.findById(officeId).orElseThrow(() -> new RuntimeException("Office not found"));

        office.setName(request.getName());
        office.setCountry(request.getCountry());
        office.setCity(request.getCity());
        office.setDistrict(request.getDistrict());
        office.setAddress(request.getAddress());
        office.setTimezone(request.getTimezone());

        officeRepository.save(office);

        return mapToResponse(office);
    }

    public void deleteOffice(UUID officeId) {

        Office office = officeRepository.findById(officeId)
                .orElseThrow(() -> new RuntimeException("Office not found"));

        officeRepository.delete(office);
    }

    private OfficeResponse mapToResponse(Office office) {
        return OfficeResponse.builder()
                .id(office.getId())
                .companyId(office.getCompany().getId())
                .companyName(office.getCompany().getName())
                .name(office.getName())
                .country(office.getCountry())
                .city(office.getCity())
                .district(office.getDistrict())
                .address(office.getAddress())
                .timezone(office.getTimezone())
                .createdAt(office.getCreatedAt())
                .updatedAt(office.getUpdatedAt())
                .build();
    }
}
