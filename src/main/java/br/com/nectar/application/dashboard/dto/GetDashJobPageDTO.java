package br.com.nectar.application.dashboard.dto;

import br.com.nectar.application.job.dto.JobsStatusFilter;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GetDashJobPageDTO {
    private Integer pageSize = 10;
    private JobsStatusFilter status = JobsStatusFilter.ALL;
    private LocalDate moth = null;
}
