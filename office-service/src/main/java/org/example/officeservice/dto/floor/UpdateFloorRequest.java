package org.example.officeservice.dto.floor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFloorRequest {

    @NotBlank
    private String name;

    @NotNull
    private Integer floorNumber;

}
