package org.example.officeservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Data;

import java.sql.Timestamp;

@Data
@MappedSuperclass
public class BaseEntity {
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @PrePersist
    protected void prePersist() {
        if (createdAt == null) {
            createdAt = new Timestamp(System.currentTimeMillis());
        }
        if(updatedAt == null){
            updatedAt = new Timestamp(System.currentTimeMillis());
        }
    }


}