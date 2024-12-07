package br.com.nectar.domain.job;

import br.com.nectar.application.beekepeer.dto.CreateBeekeeperDTO;
import br.com.nectar.application.beekepeer.dto.GetPageDTO;
import br.com.nectar.application.dashboard.dto.GetDashJobPageDTO;
import br.com.nectar.application.job.dto.CreateJobDTO;
import br.com.nectar.application.job.dto.GetJobPageDTO;
import br.com.nectar.application.job.dto.JobsStatusFilter;
import br.com.nectar.domain.address.Address;
import br.com.nectar.domain.beekeeper.Beekeeper;
import br.com.nectar.domain.beekeeper.BeekeeperService;
import br.com.nectar.domain.profile.Profile;
import br.com.nectar.domain.user.User;
import br.com.nectar.domain.user.UserService;
import br.com.nectar.domain.user.UserStatus;
import br.com.nectar.infrastructure.exceptions.FrontDisplayableException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final UserService userService;
    private final BeekeeperService beekeeperService;

    public Job create (
        CreateJobDTO createJobDTO,
        User user
    ) {
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

        job.setPesticides(createJobDTO.getPesticides());
        job.setHiveLoss(createJobDTO.getHiveLoss());

        job.setQuantityOfBales(createJobDTO.getQuantityOfBales());
        job.setWeight(createJobDTO.getWeight());
        job.setProductType(createJobDTO.getProductType());
        job.setStatus(createJobDTO.getStatus());
        job.setObservation(createJobDTO.getObservation());

        if(createJobDTO.getPostProcessing() != null) {
            var postProcessing = createJobDTO.getPostProcessing();

            job.setPostProcessingBales(postProcessing.getPostProcessingBales());
            job.setPostProcessingRevenue(postProcessing.getPostProcessingRevenue());
            job.setPostProcessingWeight(postProcessing.getPostProcessingWeight());
        }

        return jobRepository.save(job);
    }

    public Job update (
        UUID jobId,
        CreateJobDTO createJobDTO
    ) {
        var job = getById(jobId);

        job.setStatus(createJobDTO.getStatus());

        job.setOrigin(createJobDTO.getOrigin());
        job.setAppearance(createJobDTO.getAppearance());
        job.setScent(createJobDTO.getScent());
        job.setColor(createJobDTO.getColor());

        job.setPesticides(createJobDTO.getPesticides());
        job.setHiveLoss(createJobDTO.getHiveLoss());

        job.setQuantityOfBales(createJobDTO.getQuantityOfBales());
        job.setWeight(createJobDTO.getWeight());
        job.setProductType(createJobDTO.getProductType());
        job.setStatus(createJobDTO.getStatus());
        job.setObservation(createJobDTO.getObservation());

        if(createJobDTO.getPostProcessing() != null) {
            var postProcessing = createJobDTO.getPostProcessing();

            job.setPostProcessingBales(postProcessing.getPostProcessingBales());
            job.setPostProcessingRevenue(postProcessing.getPostProcessingRevenue());
            job.setPostProcessingWeight(postProcessing.getPostProcessingWeight());
        }

        return jobRepository.save(job);
    }

    public Job getById (UUID id) {
        return jobRepository.findById(id)
            .orElseThrow(() -> new FrontDisplayableException(
                HttpStatus.BAD_REQUEST,
                "Não foi possível encontrar o serviço Informado!"
            ));
    }

    public Page<Job> getPage (
        User user,
        Integer page,
        GetJobPageDTO getPageDTO
    ) {
        User org = userService.getUserOrg(user.getId());

        List<JobsStatus> status = switch (getPageDTO.getStatus()) {
            case IN_PROGRESS -> List.of(JobsStatus.IN_PROGRESS);
            case CANCELED -> List.of(JobsStatus.CANCELED);
            case CONCLUDED-> List.of(JobsStatus.CONCLUDED);
            default -> List.of(JobsStatus.values());
        };

        return jobRepository.getPageByStatus(
            org.getId(),
            status,
            PageRequest.of(
                page,
                getPageDTO.getPageSize() > 0 ? getPageDTO.getPageSize() : 10
            )
        );
    }

    public Page<Job> getPageForDash (
        User user,
        Integer page,
        GetDashJobPageDTO getPageDTO
    ) {
        User org = userService.getUserOrg(user.getId());

        List<JobsStatus> status = switch (getPageDTO.getStatus()) {
            case IN_PROGRESS -> List.of(JobsStatus.IN_PROGRESS);
            case CANCELED -> List.of(JobsStatus.CANCELED);
            case CONCLUDED-> List.of(JobsStatus.CONCLUDED);
            default -> List.of(JobsStatus.values());
        };

        return jobRepository.getPageByStatusAndDate(
            getPageDTO.getMoth().atStartOfDay(),
            getPageDTO.getMoth().atTime(23, 59, 59),
            org.getId(),
            status,
            PageRequest.of(
                page,
                getPageDTO.getPageSize() > 0 ? getPageDTO.getPageSize() : 10
            )
        );
    }

    public Page<Job> getPageByBeekeeperId (
        UUID beekeeperId,
        User user,
        Integer page,
        GetJobPageDTO getPageDTO
    ) {
        Beekeeper beekeeper = beekeeperService.getById(beekeeperId);
        User org = userService.getUserOrg(user.getId());

        List<JobsStatus> status = switch (getPageDTO.getStatus()) {
            case IN_PROGRESS -> List.of(JobsStatus.IN_PROGRESS);
            case CANCELED -> List.of(JobsStatus.CANCELED);
            case CONCLUDED-> List.of(JobsStatus.CONCLUDED);
            default -> List.of(JobsStatus.values());
        };

        return jobRepository.getPageByStatusAndBeekeeper(
            org.getId(),
            beekeeper.getId(),
            status,
            PageRequest.of(
                page,
                getPageDTO.getPageSize() > 0 ? getPageDTO.getPageSize() : 10
            )
        );
    }
}
