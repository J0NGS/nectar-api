package br.com.nectar.application.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MonthlyGraphData {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Long startedServices = 0L; // Serviços que começaram no dia
    private Long wasteOfServices = 0L; // Desperdicio dos serviços
    private Long revenueOfServices = 0L; // Arrecadado dos serviços
    private Long recivedOfServices = 0L; // Recebido dos serviços
}
