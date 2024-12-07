package br.com.nectar.application.job.dto;

import lombok.Data;

@Data
public class GetJobPageDTO {
    private Integer pageSize = 10;
    private JobsStatusFilter status = JobsStatusFilter.ALL;
}
