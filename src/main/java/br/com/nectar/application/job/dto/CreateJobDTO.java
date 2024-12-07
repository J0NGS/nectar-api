package br.com.nectar.application.job.dto;

import br.com.nectar.domain.job.JobsStatus;
import br.com.nectar.domain.job.ProductType;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateJobDTO {
    private JobsStatus status = JobsStatus.IN_PROGRESS;
    private String origin; // Origem
    private String appearance; // Aparência
    private String scent; // Cheiro
    private String color; // Cor

    private Boolean pesticides; // Pesticidas
    private Boolean hiveLoss; // Perda de enxame

    private Integer quantityOfBales; // Quantidade de fardos
    private Integer weight; // Peso total
    private UUID beekeeperId;
    private ProductType productType = ProductType.WAX;

    private PostProcessingDTO postProcessing = null;

    private String observation = null;
}
