package org.example.officeservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.officeservice.dto.floor.CreateFloorRequest;
import org.example.officeservice.dto.floor.FloorResponse;
import org.example.officeservice.dto.floor.UpdateFloorRequest;
import org.example.officeservice.service.FloorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/floors")
@RequiredArgsConstructor
public class FloorController {

    private final FloorService floorService;

    @PostMapping
    public ResponseEntity<FloorResponse> createFloor(@Valid @RequestBody CreateFloorRequest request) {
        FloorResponse response = floorService.createFloor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{floorId}")
    public ResponseEntity<FloorResponse> getFloorById(@PathVariable UUID floorId) {
        FloorResponse response = floorService.getFloorById(floorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FloorResponse>> getAllFloors() {
        List<FloorResponse> response = floorService.getAllFloors();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{floorId}")
    public ResponseEntity<FloorResponse> updateFloor(@PathVariable UUID floorId, @Valid @RequestBody UpdateFloorRequest request) {
        FloorResponse response = floorService.updateFloor(floorId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{floorId}")
    public ResponseEntity<Void> deleteFloor(@PathVariable UUID floorId) {
        floorService.deleteFloor(floorId);
        return ResponseEntity.noContent().build();
    }
}
