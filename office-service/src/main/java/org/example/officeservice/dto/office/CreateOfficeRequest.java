package org.example.officeservice.dto.office;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOfficeRequest {

    @NotNull
    private UUID companyId;

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
