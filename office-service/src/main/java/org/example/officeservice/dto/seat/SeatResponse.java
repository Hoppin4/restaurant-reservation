package org.example.officeservice.dto.seat;

import lombok.*;
import org.example.officeservice.enums.enums;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponse {

    private UUID id;

    private UUID floorId;

    private String floorName;

    private String seatCode;

    private enums.SeatType seatType;

    private enums.SeatStatus status;

    private Boolean hasMonitor;

    private Boolean hasDockStation;

    private Boolean nearWindow;

    private Timestamp createdAt;

    private Timestamp updatedAt;

}
