package br.com.nectar.domain.job;

import br.com.nectar.application.dashboard.dto.GetDashJobPageDTO;
import br.com.nectar.application.dashboard.dto.MonthlyBoardDTO;
import br.com.nectar.application.dashboard.dto.MonthlyGraphDTO;
import br.com.nectar.application.dashboard.dto.MonthlyGraphData;
import br.com.nectar.application.job.dto.CreateJobDTO;
import br.com.nectar.application.job.dto.GetJobPageDTO;
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
public class JobService {
    private final JobRepository jobRepository;
    private final UserService userService;
    private final BeekeeperService beekeeperService;

    private Integer calculateWasteRate(Integer initialWeight, Integer waste) {
        if (initialWeight == null || initialWeight <= 0 || waste == null || waste <= 0) {
            return 0; // Retorna 0 caso algum valor seja inválido
        }
        
        
        
        Integer wasteRate = ((waste / initialWeight) * 100);
        
        return Math.round(wasteRate);
    }

    public Job create(
            CreateJobDTO createJobDTO,
            User user) {
        User org = userService.getUserOrg(user.getId());
        Beekeeper beekeeper = beekeeperService.getById(createJobDTO.getBeekeeperId());

        var job = new Job();

        job.setOwner(user);
        job.setOrg(org);

        job.setBeekeeper(beekeeper);

        job.setOrigin(createJobDTO.getOrigin());
        job.setAppearance(createJobDTO.getAppearance());
        job.setScent(createJobDTO.getScent());
        job.setColor(createJobDTO.getColor());
        job.setStartAt(createJobDTO.getStartAt());

        job.setPesticides(createJobDTO.getPesticides());
        job.setHiveLoss(createJobDTO.getHiveLoss());
        job.setQuantityOfBales(createJobDTO.getQuantityOfBales());
        job.setProductType(createJobDTO.getProductType());
        job.setStatus(createJobDTO.getStatus());
        job.setObservation(createJobDTO.getObservation());

        if (createJobDTO.getWeight() == null || createJobDTO.getWeight() <= 0) {
            throw new FrontDisplayableException(
                    HttpStatus.BAD_REQUEST,
                    "O peso do serviço deve ser maior que 0!");
            
        }
        job.setWeight((createJobDTO.getWeight()));

        if (createJobDTO.getPostProcessing() != null) {
            var postProcessing = createJobDTO.getPostProcessing();

            job.setWaste((postProcessing.getWaste()));
            job.setPostProcessingBales(postProcessing.getPostProcessingBales());
            job.setPostProcessingRevenue((postProcessing.getPostProcessingRevenue()));
            job.setPostProcessingWeight((postProcessing.getPostProcessingWeight()));

            job.setWasteRate(calculateWasteRate(createJobDTO.getWeight(), postProcessing.getWaste()));

            job.setStatus(JobsStatus.CONCLUDED);
        }

        return jobRepository.save(job);
    }

    public Job update(
            UUID jobId,
            CreateJobDTO createJobDTO) {
        var job = getById(jobId);

        job.setStatus(createJobDTO.getStatus());

        job.setOrigin(createJobDTO.getOrigin());
        job.setAppearance(createJobDTO.getAppearance());
        job.setScent(createJobDTO.getScent());
        job.setColor(createJobDTO.getColor());
        job.setStartAt(createJobDTO.getStartAt());

        job.setPesticides(createJobDTO.getPesticides());
        job.setHiveLoss(createJobDTO.getHiveLoss());

        job.setQuantityOfBales(createJobDTO.getQuantityOfBales());
        if (createJobDTO.getWeight() == null || createJobDTO.getWeight() <= 0) {
            throw new FrontDisplayableException(
                    HttpStatus.BAD_REQUEST,
                    "O peso do serviço deve ser maior que 0!");
            
        }
        job.setWeight((createJobDTO.getWeight()));
        job.setProductType(createJobDTO.getProductType());
        job.setStatus(createJobDTO.getStatus());
        job.setObservation(createJobDTO.getObservation());

        if (createJobDTO.getPostProcessing() != null) {
            var postProcessing = createJobDTO.getPostProcessing();

            job.setWaste((postProcessing.getWaste()));
            job.setPostProcessingBales(postProcessing.getPostProcessingBales());
            job.setPostProcessingRevenue((postProcessing.getPostProcessingRevenue()));
            job.setPostProcessingWeight((postProcessing.getPostProcessingWeight())  );
            job.setWasteRate(calculateWasteRate(createJobDTO.getWeight(), postProcessing.getWaste()));

            job.setStatus(JobsStatus.CONCLUDED);
        }

        return jobRepository.save(job);
    }

    public Job getById(UUID id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new FrontDisplayableException(
                        HttpStatus.BAD_REQUEST,
                        "Não foi possível encontrar o serviço Informado!"));
    }

    public Page<Job> getPage(
            User user,
            Integer page,
            GetJobPageDTO getPageDTO) {
        User org = userService.getUserOrg(user.getId());

        List<JobsStatus> status = switch (getPageDTO.getStatus()) {
            case IN_PROGRESS -> List.of(JobsStatus.IN_PROGRESS);
            case CANCELED -> List.of(JobsStatus.CANCELED);
            case CONCLUDED -> List.of(JobsStatus.CONCLUDED);
            default -> List.of(JobsStatus.values());
        };

        return jobRepository.getPageByStatus(
                org.getId(),
                status,
                PageRequest.of(
                        page,
                        getPageDTO.getPageSize() > 0 ? getPageDTO.getPageSize() : 10));
    }

    public Page<Job> getPageForDash(
            User user,
            Integer page,
            GetDashJobPageDTO getPageDTO) {
        User org = userService.getUserOrg(user.getId());

        List<JobsStatus> status = switch (getPageDTO.getStatus()) {
            case IN_PROGRESS -> List.of(JobsStatus.IN_PROGRESS);
            case CANCELED -> List.of(JobsStatus.CANCELED);
            case CONCLUDED -> List.of(JobsStatus.CONCLUDED);
            default -> List.of(JobsStatus.values());
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

    public Page<Job> getPageByBeekeeperId(
            UUID beekeeperId,
            User user,
            Integer page,
            GetJobPageDTO getPageDTO) {
        Beekeeper beekeeper = beekeeperService.getById(beekeeperId);
        User org = userService.getUserOrg(user.getId());

        List<JobsStatus> status = switch (getPageDTO.getStatus()) {
            case IN_PROGRESS -> List.of(JobsStatus.IN_PROGRESS);
            case CANCELED -> List.of(JobsStatus.CANCELED);
            case CONCLUDED -> List.of(JobsStatus.CONCLUDED);
            default -> List.of(JobsStatus.values());
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
                List.of(JobsStatus.CONCLUDED, JobsStatus.IN_PROGRESS));

        var start = month.with(TemporalAdjusters.firstDayOfMonth());
        var data = new ArrayList<MonthlyGraphData>();

        while (start.isBefore(end)) {
            LocalDate finalStart = start;

            var jobsForDay = jobsInMonth.stream()
                    .filter(job -> job.getCreatedAt().toLocalDate().isEqual(finalStart))
                    .toList();

            if (!jobsForDay.isEmpty()) {
                var qtd = jobsForDay.size();

                var mediaRecived = jobsForDay.stream()
                        .map(Job::getWeight)
                        .filter(Objects::nonNull)
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0);

                var mediaWaste = jobsForDay.stream()
                        .map(Job::getWasteRate)
                        .filter(Objects::nonNull)
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0);

                var mediaRevenue = jobsForDay.stream()
                        .map(Job::getPostProcessingRevenue)
                        .filter(Objects::nonNull)
                        .mapToInt(Integer::intValue)
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
                List.of(JobsStatus.CONCLUDED, JobsStatus.IN_PROGRESS));
    
        // Convertendo stream para lista para poder reutilizar a stream        
        var jobsConcludes = jobsInMonth.stream()
                .filter(job -> job.getStatus().equals(JobsStatus.CONCLUDED))
                .toList();
    
        var concludeProcess = jobsConcludes.size();
    
        var revenue = concludeProcess > 0
                ? jobsConcludes.stream().mapToInt(Job::getPostProcessingRevenue).sum()
                : 0;
    
        var waste = concludeProcess > 0
                ? jobsConcludes.stream().mapToInt(Job::getWaste).sum()
                : 0;
    
        var inProgress = jobsInMonth.stream()
                .filter(job -> job.getStatus().equals(JobsStatus.IN_PROGRESS))
                .count();
    
        var newBeekeepers = beekeeperService.getQtdNewInMonth(org, month);
    
        board.setRevenue((long) revenue);
        board.setWaste((long) waste);
        board.setConcludeServices((long) concludeProcess);
        board.setInProcessingServices(inProgress);
        board.setNewBeekeepers(newBeekeepers);
        board.setWeight((long) jobsInMonth.stream().mapToInt(Job::getWeight).sum());
    
        return board;
    }
    
}
