package br.com.nectar.domain.work;

import br.com.nectar.domain.beekeeper.Beekeeper;
import br.com.nectar.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "works")
@Setter
@Getter
public class Work {
    @Id
    private UUID id = UUID.randomUUID();

    private String origin;
    private String appearance;
    private String scent;
    private String color;
    private Integer weight;

    private Integer postProcessingWeight;
    private Integer postProcessingRevenue;
    private Integer postProcessingDelivered;
    private Integer postProcessingResidue;
    private Integer residueRate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startAt;

    private String observation;

    @Enumerated(EnumType.STRING)
    private ProductType productType = ProductType.WAX;

    @Enumerated(EnumType.STRING)
    private WorkStatus status = WorkStatus.IN_PROGRESS;

    @OneToOne
    @JoinColumn(name = "beekeeper_id", referencedColumnName = "id")
    private Beekeeper beekeeper;

    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "org_id", referencedColumnName = "id")
    private User org;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();
}
