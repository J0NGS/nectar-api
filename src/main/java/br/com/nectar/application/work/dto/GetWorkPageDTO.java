package br.com.nectar.application.work.dto;

import lombok.Data;

@Data
public class GetWorkPageDTO {
    private Integer pageSize = 10;
    private WorkStatusFilter status = WorkStatusFilter.ALL;
}
