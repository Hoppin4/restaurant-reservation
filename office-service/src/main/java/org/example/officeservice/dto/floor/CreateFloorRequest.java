package org.example.officeservice.dto.floor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFloorRequest {

    @NotNull
    private UUID officeId;

    @NotBlank
    private String name;

    @NotNull
    private Integer floorNumber;

}
