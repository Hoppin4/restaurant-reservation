package org.example.officeservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.officeservice.enums.enums;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "seats")
public class Seat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    @Column(nullable = false, length = 20)
    private String seatCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private enums.SeatType seatType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private enums.SeatStatus status;

    @Column(nullable = false)
    private Boolean hasMonitor;

    @Column(nullable = false)
    private Boolean hasDockStation;

    @Column(nullable = false)
    private Boolean nearWindow;

}
