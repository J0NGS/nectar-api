package br.com.nectar.application.job.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GetJobPageDTO {
    private Integer pageSize = 10;
    private JobsStatusFilter status = JobsStatusFilter.ALL;
    private LocalDate moth = null;
}
