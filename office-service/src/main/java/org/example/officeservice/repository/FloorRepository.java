package org.example.officeservice.repository;

import org.example.officeservice.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FloorRepository extends JpaRepository<Floor, UUID> {
}
