package br.com.nectar.application.dashboard.dto;

import br.com.nectar.application.work.dto.WorkStatusFilter;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GetDashJobPageDTO {
    private Integer pageSize = 10;
    private WorkStatusFilter status = WorkStatusFilter.ALL;
    private LocalDate month = null;
}
