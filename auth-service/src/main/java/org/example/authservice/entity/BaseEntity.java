package org.example.authservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

import java.sql.Timestamp;

@MappedSuperclass
public class BaseEntity {
    @Column(name = "created_at", updatable = false)
    private Timestamp createdDate;

    @PrePersist
    protected void prePersist() {
        if (createdDate == null) {
            createdDate = new Timestamp(System.currentTimeMillis());
        }
    }
}