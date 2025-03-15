package br.com.nectar.domain.work;

import br.com.nectar.application.dashboard.dto.GetDashJobPageDTO;
import br.com.nectar.application.dashboard.dto.MonthlyBoardDTO;
import br.com.nectar.application.dashboard.dto.MonthlyGraphDTO;
import br.com.nectar.application.dashboard.dto.MonthlyGraphData;
import br.com.nectar.application.work.dto.CreateWorkDTO;
import br.com.nectar.application.work.dto.GetWorkPageDTO;
import br.com.nectar.domain.beekeeper.Beekeeper;
import br.com.nectar.domain.beekeeper.BeekeeperService;
import br.com.nectar.domain.user.User;
import br.com.nectar.domain.user.UserService;
import br.com.nectar.infrastructure.exceptions.FrontDisplayableException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkService {
    private final WorkRepository jobRepository;
    private final UserService userService;
    private final BeekeeperService beekeeperService;

    private Long calculateResidueRate(Long initialWeight, Long waste) {
        if (initialWeight == null || initialWeight <= 0 || waste == null || waste <= 0)
            return 0L; // Retorna 0 caso algum valor seja inválido

        var wasteRate = ((waste / initialWeight) * 100);
        return wasteRate;
    }

    public Work create(
        CreateWorkDTO createWorkDTO,
        User user
    ) {
        User org = userService.getUserOrg(user.getId());
        Beekeeper beekeeper = beekeeperService.getById(createWorkDTO.getBeekeeperId());

        var job = new Work();

        job.setOwner(user);
        job.setOrg(org);

        job.setBeekeeper(beekeeper);

        job.setOrigin(createWorkDTO.getOrigin());
        job.setAppearance(createWorkDTO.getAppearance());
        job.setScent(createWorkDTO.getScent());
        job.setColor(createWorkDTO.getColor());
        job.setStartAt(createWorkDTO.getStartAt());
        job.setProductType(createWorkDTO.getProductType());
        job.setStatus(createWorkDTO.getStatus());
        job.setObservation(createWorkDTO.getObservation());

        if (createWorkDTO.getWeight() == null || createWorkDTO.getWeight() <= 0) {
            throw new FrontDisplayableException(
                    HttpStatus.BAD_REQUEST,
                    "O peso do serviço deve ser maior que 0!");
            
        }
        job.setWeight((createWorkDTO.getWeight()));

        if (createWorkDTO.getPostProcessing() != null) {
            var postProcessing = createWorkDTO.getPostProcessing();

            job.setPostProcessingResidue((postProcessing.getPostProcessingResidue()));
            job.setPostProcessingDelivered(postProcessing.getPostProcessingDelivered());
            job.setPostProcessingRevenue((postProcessing.getPostProcessingRevenue()));
            job.setPostProcessingWeight((postProcessing.getPostProcessingWeight()));

            job.setResidueRate(
                calculateResidueRate(createWorkDTO.getWeight(), postProcessing.getPostProcessingResidue())
            );

            job.setStatus(WorkStatus.CONCLUDED);
        }

        return jobRepository.save(job);
    }

    public Work update(
        UUID jobId,
        CreateWorkDTO createWorkDTO
    ) {
        if (createWorkDTO.getWeight() == null || createWorkDTO.getWeight() <= 0) {
            throw new FrontDisplayableException(
                HttpStatus.BAD_REQUEST,
                "O peso do serviço deve ser maior que 0!");

        }

        var job = getById(jobId);

        job.setStatus(createWorkDTO.getStatus());
        job.setOrigin(createWorkDTO.getOrigin());
        job.setAppearance(createWorkDTO.getAppearance());
        job.setScent(createWorkDTO.getScent());
        job.setColor(createWorkDTO.getColor());
        job.setStartAt(createWorkDTO.getStartAt());
        job.setWeight((createWorkDTO.getWeight()));
        job.setProductType(createWorkDTO.getProductType());
        job.setStatus(createWorkDTO.getStatus());
        job.setObservation(createWorkDTO.getObservation());

        if (createWorkDTO.getPostProcessing() != null) {
            var postProcessing = createWorkDTO.getPostProcessing();

            job.setPostProcessingResidue((postProcessing.getPostProcessingResidue()));
            job.setPostProcessingDelivered(postProcessing.getPostProcessingDelivered());
            job.setPostProcessingRevenue((postProcessing.getPostProcessingRevenue()));
            job.setPostProcessingWeight((postProcessing.getPostProcessingWeight()));

            job.setResidueRate(
                calculateResidueRate(createWorkDTO.getWeight(), postProcessing.getPostProcessingResidue())
            );
            job.setStatus(WorkStatus.CONCLUDED);
        }

        return jobRepository.save(job);
    }

    public Work getById(UUID id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new FrontDisplayableException(
                        HttpStatus.BAD_REQUEST,
                        "Não foi possível encontrar o serviço Informado!"));
    }

    public Page<Work> getPage(
            User user,
            Integer page,
            GetWorkPageDTO getPageDTO) {
        User org = userService.getUserOrg(user.getId());

        List<WorkStatus> status = switch (getPageDTO.getStatus()) {
            case IN_PROGRESS -> List.of(WorkStatus.IN_PROGRESS);
            case CANCELED -> List.of(WorkStatus.CANCELED);
            case CONCLUDED -> List.of(WorkStatus.CONCLUDED);
            default -> List.of(WorkStatus.values());
        };

        return jobRepository.getPageByStatus(
                org.getId(),
                status,
                PageRequest.of(
                        page,
                        getPageDTO.getPageSize() > 0 ? getPageDTO.getPageSize() : 10));
    }

    public Page<Work> getPageForDash(
            User user,
            Integer page,
            GetDashJobPageDTO getPageDTO) {
        User org = userService.getUserOrg(user.getId());

        List<WorkStatus> status = switch (getPageDTO.getStatus()) {
            case IN_PROGRESS -> List.of(WorkStatus.IN_PROGRESS);
            case CANCELED -> List.of(WorkStatus.CANCELED);
            case CONCLUDED -> List.of(WorkStatus.CONCLUDED);
            default -> List.of(WorkStatus.values());
        };

        var init = getPageDTO.getMonth().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        var end = getPageDTO.getMonth().with(TemporalAdjusters.lastDayOfMonth()).plusDays(1).atTime(23, 59, 59);

        return jobRepository.getPageByStatusAndDate(
            init,
            end,
            org.getId(),
            status,
            PageRequest.of(
                page,
                getPageDTO.getPageSize() > 0 ? getPageDTO.getPageSize() : 10,
                Sort.by(Sort.Direction.DESC, "createdAt")
            )
        );
    }

    public Page<Work> getPageByBeekeeperId(
            UUID beekeeperId,
            User user,
            Integer page,
            GetWorkPageDTO getPageDTO) {
        Beekeeper beekeeper = beekeeperService.getById(beekeeperId);
        User org = userService.getUserOrg(user.getId());

        List<WorkStatus> status = switch (getPageDTO.getStatus()) {
            case IN_PROGRESS -> List.of(WorkStatus.IN_PROGRESS);
            case CANCELED -> List.of(WorkStatus.CANCELED);
            case CONCLUDED -> List.of(WorkStatus.CONCLUDED);
            default -> List.of(WorkStatus.values());
        };

        return jobRepository.getPageByStatusAndBeekeeper(
                org.getId(),
                beekeeper.getId(),
                status,
                PageRequest.of(
                        page,
                        getPageDTO.getPageSize() > 0 ? getPageDTO.getPageSize() : 10));
    }

    public MonthlyGraphDTO getMonthlyGraph(
            User user,
            LocalDate month) {
        var graphData = new MonthlyGraphDTO();

        var org = userService.getUserOrg(user.getId());

        var init = month.with(TemporalAdjusters.firstDayOfMonth());
        var end = month.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1);

        var jobsInMonth = jobRepository.getAllByStatus(
                init.atStartOfDay(),
                end.atTime(23, 59, 59),
                org.getId(),
                List.of(WorkStatus.CONCLUDED, WorkStatus.IN_PROGRESS));

        var start = month.with(TemporalAdjusters.firstDayOfMonth());
        var data = new ArrayList<MonthlyGraphData>();

        while (start.isBefore(end)) {
            LocalDate finalStart = start;

            var jobsForDay = jobsInMonth.stream()
                    .filter(work -> work.getCreatedAt().toLocalDate().isEqual(finalStart))
                    .toList();

            if (!jobsForDay.isEmpty()) {
                var qtd = jobsForDay.size();

                var mediaRecived = jobsForDay.stream()
                        .map(Work::getWeight)
                        .filter(Objects::nonNull)
                        .mapToInt(Long::intValue)
                        .average()
                        .orElse(0);

                var mediaWaste = jobsForDay.stream()
                        .map(Work::getPostProcessingResidue)
                        .filter(Objects::nonNull)
                        .mapToInt(Long::intValue)
                        .average()
                        .orElse(0);

                var mediaRevenue = jobsForDay.stream()
                        .map(Work::getPostProcessingRevenue)
                        .filter(Objects::nonNull)
                        .mapToInt(Long::intValue)
                        .average()
                        .orElse(0);

                data.add(
                    new MonthlyGraphData(
                        start,
                        (long) qtd,
                        (long) mediaWaste,
                        (long) mediaRevenue,
                        (long) mediaRecived
                    )
                );
            } else {
                data.add(
                    new MonthlyGraphData(
                        start,
                        0L,
                        0L,
                        0L,
                        0L
                    )
                );
            }

            start = start.plusDays(1);
        }

        graphData.setData(data);
        graphData.setMonth(month);

        return graphData;
    }

    public MonthlyBoardDTO getMonthlyBoard(User user, LocalDate month) {
        var board = new MonthlyBoardDTO();
    
        var org = userService.getUserOrg(user.getId());
    
        var init = month.with(TemporalAdjusters.firstDayOfMonth());
        var end = month.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1);
    
        var jobsInMonth = jobRepository.getAllByStatus(
                init.atStartOfDay(),
                end.atTime(23, 59, 59),
                org.getId(),
                List.of(WorkStatus.CONCLUDED, WorkStatus.IN_PROGRESS));
    
        // Convertendo stream para lista para poder reutilizar a stream        
        var jobsConcludes = jobsInMonth.stream()
                .filter(work -> work.getStatus().equals(WorkStatus.CONCLUDED))
                .toList();
    
        var concludeProcess = jobsConcludes.size();
    
        var revenue = concludeProcess > 0
                ? jobsConcludes.stream().mapToLong(Work::getPostProcessingRevenue).sum()
                : 0;
    
        var waste = concludeProcess > 0
                ? jobsConcludes.stream().mapToLong(Work::getPostProcessingResidue).sum()
                : 0;
    
        var inProgress = jobsInMonth.stream()
                .filter(work -> work.getStatus().equals(WorkStatus.IN_PROGRESS))
                .count();
    
        var newBeekeepers = beekeeperService.getQtdNewInMonth(org, month);
    
        board.setRevenue( revenue);
        board.setWaste(waste);
        board.setConcludeServices((long) concludeProcess);
        board.setInProcessingServices(inProgress);
        board.setNewBeekeepers(newBeekeepers);
        board.setWeight(jobsInMonth.stream().mapToLong(Work::getWeight).sum());
    
        return board;
    }
    
}
