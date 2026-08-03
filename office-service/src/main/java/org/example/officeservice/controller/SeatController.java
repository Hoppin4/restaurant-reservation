package org.example.officeservice.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.officeservice.dto.seat.CreateSeatRequest;
import org.example.officeservice.dto.seat.SeatResponse;
import org.example.officeservice.dto.seat.UpdateSeatRequest;
import org.example.officeservice.service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @PostMapping
    public ResponseEntity<SeatResponse> createSeat(@Valid @RequestBody CreateSeatRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seatService.createSeat(request));
    }

    @GetMapping("/{seatId}")
    public ResponseEntity<SeatResponse> getSeatById(@PathVariable UUID seatId) {
        return ResponseEntity.ok(seatService.getSeatById(seatId));
    }

    @GetMapping
    public ResponseEntity<List<SeatResponse>> getAllSeats() {
        return ResponseEntity.ok(seatService.getAllSeats());
    }

    @PutMapping("/{seatId}")
    public ResponseEntity<SeatResponse> updateSeat(@PathVariable UUID seatId, @Valid @RequestBody UpdateSeatRequest request) {
        return ResponseEntity.ok(seatService.updateSeat(seatId, request));
    }

    @DeleteMapping("/{seatId}")
    public ResponseEntity<Void> deleteSeat(@PathVariable UUID seatId) {
        seatService.deleteSeat(seatId);

        return ResponseEntity.noContent().build();
    }
}
