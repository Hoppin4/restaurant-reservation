package org.example.officeservice.repository;

import org.example.officeservice.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OfficeRepository extends JpaRepository<Office,UUID> {
}
