package br.com.nectar.application.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyBoardDTO {
    private Long inProcessingServices = 0L;
    private Long ConcludeServices = 0L;
    private Long newBeekeepers = 0L;
    private Long revenue = 0L;
    private Long waste = 0L;
    private Long weight = 0L;
}
