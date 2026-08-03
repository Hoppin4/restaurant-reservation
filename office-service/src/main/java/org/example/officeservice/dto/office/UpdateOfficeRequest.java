package org.example.officeservice.dto.office;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOfficeRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String country;

    @NotBlank
    private String city;

    @NotBlank
    private String district;

    @NotBlank
    private String address;

    @NotBlank
    private String timezone;

}
