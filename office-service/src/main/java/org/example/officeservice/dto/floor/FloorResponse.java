package org.example.officeservice.dto.floor;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FloorResponse {

    private UUID id;

    private UUID officeId;

    private String officeName;

    private String name;

    private Integer floorNumber;

    private Timestamp createdAt;

    private Timestamp updatedAt;

}
