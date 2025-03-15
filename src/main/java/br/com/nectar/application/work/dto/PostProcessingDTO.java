package br.com.nectar.application.work.dto;

import lombok.Data;

@Data
public class PostProcessingDTO {
    private Integer postProcessingWeight;
    private Integer postProcessingRevenue;
    private Integer postProcessingDelivered;
    private Integer postProcessingResidue;
}
