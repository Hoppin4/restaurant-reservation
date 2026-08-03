package org.example.officeservice.dto.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse {

    private UUID id;

    private String name;

    private String taxNumber;

    private String email;

    private String phone;

    private Timestamp createdAt;

    private Timestamp updatedAt;

}
