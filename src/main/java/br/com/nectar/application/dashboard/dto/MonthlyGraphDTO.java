package br.com.nectar.application.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Data
public class MonthlyGraphDTO {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate month;
    private List<MonthlyGraphData> data = Collections.emptyList();
}


