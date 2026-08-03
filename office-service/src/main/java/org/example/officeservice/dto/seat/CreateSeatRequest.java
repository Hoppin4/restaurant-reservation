package org.example.officeservice.dto.seat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.officeservice.enums.enums;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSeatRequest {
    @NotNull
    private UUID floorId;

    @NotBlank
    private String seatCode;

    @NotNull
    private enums.SeatType seatType;

    @NotNull
    private enums.SeatStatus status;

    @NotNull
    private Boolean hasMonitor;

    @NotNull
    private Boolean hasDockStation;

    @NotNull
    private Boolean nearWindow;

}
