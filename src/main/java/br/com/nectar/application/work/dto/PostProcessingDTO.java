package br.com.nectar.application.work.dto;

import lombok.Data;

@Data
public class PostProcessingDTO {
    private Long postProcessingWeight;
    private Long postProcessingRevenue;
    private Long postProcessingDelivered;
    private Long postProcessingResidue;
}
