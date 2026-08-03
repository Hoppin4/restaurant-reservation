package org.example.officeservice.dto.office;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfficeResponse {

    private UUID id;

    private UUID companyId;

    private String companyName;

    private String name;

    private String country;

    private String city;

    private String district;

    private String address;

    private String timezone;

    private Timestamp createdAt;

    private Timestamp updatedAt;

}
