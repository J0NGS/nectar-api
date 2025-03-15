package br.com.nectar.application.work.dto;

import br.com.nectar.domain.work.WorkStatus;
import br.com.nectar.domain.work.ProductType;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateWorkDTO {
    private WorkStatus status = WorkStatus.IN_PROGRESS;
    private ProductType productType = ProductType.WAX;

    private String origin;
    private String appearance;
    private String scent;
    private String color;
    private Long weight;
    private UUID beekeeperId;
    private LocalDate startAt;

    private PostProcessingDTO postProcessing = null;
    private String observation = null;
}
