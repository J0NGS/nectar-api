package br.com.nectar.application.job.dto;

import lombok.Data;

@Data
public class PostProcessingDTO {
    private Integer postProcessingBales; // Quantidade de fardos pós-processamento
    private Integer postProcessingWeight; // Peso total pós-processamento
    private Integer postProcessingRevenue; // Arrecadado
    private Integer waste; // Peso desperdiçado
}
