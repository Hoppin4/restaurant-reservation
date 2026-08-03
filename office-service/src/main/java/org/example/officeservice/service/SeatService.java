package org.example.officeservice.service;

import org.example.officeservice.dto.seat.CreateSeatRequest;
import org.example.officeservice.dto.seat.SeatResponse;
import org.example.officeservice.dto.seat.UpdateSeatRequest;
import org.example.officeservice.entity.Floor;
import org.example.officeservice.entity.Seat;
import org.example.officeservice.repository.FloorRepository;
import org.example.officeservice.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SeatService {
    private final SeatRepository seatRepository;
    private final FloorRepository floorRepository;

    public SeatService(SeatRepository seatRepository, FloorRepository floorRepository) {
        this.seatRepository = seatRepository;
        this.floorRepository = floorRepository;
    }

    public SeatResponse createSeat(CreateSeatRequest request) {
        Floor floor = floorRepository.findById(request.getFloorId()).orElseThrow(() -> new RuntimeException("Floor not found"));

        Seat seat = Seat.builder()
                .floor(floor)
                .seatCode(request.getSeatCode())
                .seatType(request.getSeatType())
                .status(request.getStatus())
                .hasMonitor(request.getHasMonitor())
                .hasDockStation(request.getHasDockStation())
                .nearWindow(request.getNearWindow())
                .build();

        seatRepository.save(seat);
        return mapToResponse(seat);
    }


    public SeatResponse getSeatById(UUID seatId) {
        Seat seat = seatRepository.findById(seatId).orElseThrow(() -> new RuntimeException("Seat not found"));
        return mapToResponse(seat);
    }

    public List<SeatResponse> getAllSeats() {
        return seatRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public SeatResponse updateSeat(UUID seatId, UpdateSeatRequest request) {
        Seat seat = seatRepository.findById(seatId).orElseThrow(() -> new RuntimeException("Seat not found"));

        seat.setSeatCode(request.getSeatCode());
        seat.setSeatType(request.getSeatType());
        seat.setStatus(request.getStatus());
        seat.setHasMonitor(request.getHasMonitor());
        seat.setHasDockStation(request.getHasDockStation());
        seat.setNearWindow(request.getNearWindow());

        seatRepository.save(seat);

        return mapToResponse(seat);
    }

    public void deleteSeat(UUID seatId) {
        Seat seat = seatRepository.findById(seatId).orElseThrow(() -> new RuntimeException("Seat not found"));

        seatRepository.delete(seat);
    }

    private SeatResponse mapToResponse(Seat seat) {
        return SeatResponse.builder()
                .id(seat.getId())
                .floorId(seat.getFloor().getId())
                .floorName(seat.getFloor().getName())
                .seatCode(seat.getSeatCode())
                .seatType(seat.getSeatType())
                .status(seat.getStatus())
                .hasMonitor(seat.getHasMonitor())
                .hasDockStation(seat.getHasDockStation())
                .nearWindow(seat.getNearWindow())
                .createdAt(seat.getCreatedAt())
                .updatedAt(seat.getUpdatedAt())
                .build();
    }
}
