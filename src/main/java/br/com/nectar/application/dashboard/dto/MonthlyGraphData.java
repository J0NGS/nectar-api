package br.com.nectar.application.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MonthlyGraphData {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Long startedServices = 0L; // Serviços que começaram no dia
    private Long MediaWasteOfServices = 0L; // Média de desperdicio dos serviços
    private Long MediaRevenueOfServices = 0L; // Média de arrecadado dos serviços
}
