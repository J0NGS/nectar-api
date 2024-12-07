package br.com.nectar.domain.job;

import br.com.nectar.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "jobs")
@Setter
@Getter
public class Job {
    @Id
    private UUID id = UUID.randomUUID();

    private String origin; // Origem
    private String appearance; // Aparência
    private String scent; // Cheiro
    private String color; // Cor

    private Boolean pesticides; // Pesticidas
    private Boolean hiveLoss; // Perda de enxame

    private Integer quantityOfBales; // Quantidade de fardos
    private Integer weight; // Peso total

    private Integer postProcessingBales; // Quantidade de fardos pós-processamento
    private Integer postProcessingWeight; // Peso total pós-processamento
    private Integer postProcessingRevenue; // Arrecadado
    private Integer wasteRate; // Taxa de desperdício

    private String observation;

    @Enumerated(EnumType.STRING)
    private ProductType productType = ProductType.WAX;

    @Enumerated(EnumType.STRING)
    private JobsStatus status = JobsStatus.IN_PROGRESS;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User beekeeper;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();
}
