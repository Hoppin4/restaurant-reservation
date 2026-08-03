package org.example.officeservice.service;

import org.example.officeservice.dto.floor.CreateFloorRequest;
import org.example.officeservice.dto.floor.FloorResponse;
import org.example.officeservice.dto.floor.UpdateFloorRequest;
import org.example.officeservice.entity.Floor;
import org.example.officeservice.entity.Office;
import org.example.officeservice.repository.FloorRepository;
import org.example.officeservice.repository.OfficeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FloorService {
    private final FloorRepository floorRepository;
    private final OfficeRepository officeRepository;

    public FloorService(FloorRepository floorRepository, OfficeRepository officeRepository) {
        this.floorRepository = floorRepository;
        this.officeRepository = officeRepository;
    }

    public FloorResponse createFloor(CreateFloorRequest request) {
        Office office = officeRepository.findById(request.getOfficeId()).orElseThrow(() -> new RuntimeException("Office not found"));

        Floor floor = Floor.builder()
                .office(office)
                .name(request.getName())
                .floorNumber(request.getFloorNumber())
                .build();

        floorRepository.save(floor);
        return mapToResponse(floor);
    }

    public FloorResponse getFloorById(UUID floorId) {
        Floor floor = floorRepository.findById(floorId).orElseThrow(() -> new RuntimeException("Floor not found"));
        return mapToResponse(floor);
    }

    public List<FloorResponse> getAllFloors() {
        return floorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public FloorResponse updateFloor(UUID floorId, UpdateFloorRequest request) {
        Floor floor = floorRepository.findById(floorId).orElseThrow(() -> new RuntimeException("Floor not found"));

        floor.setName(request.getName());
        floor.setFloorNumber(request.getFloorNumber());

        floorRepository.save(floor);

        return mapToResponse(floor);
    }

    public void deleteFloor(UUID floorId) {
        Floor floor = floorRepository.findById(floorId).orElseThrow(() -> new RuntimeException("Floor not found"));

        floorRepository.delete(floor);
    }

    private FloorResponse mapToResponse(Floor floor) {
        return FloorResponse.builder()
                .id(floor.getId())
                .officeId(floor.getOffice().getId())
                .officeName(floor.getOffice().getName())
                .name(floor.getName())
                .floorNumber(floor.getFloorNumber())
                .createdAt(floor.getCreatedAt())
                .updatedAt(floor.getUpdatedAt())
                .build();
    }
}
