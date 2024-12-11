package br.com.nectar.domain.beekeeper;

import br.com.nectar.application.beekepeer.dto.CreateBeekeeperDTO;
import br.com.nectar.application.beekepeer.dto.GetPageDTO;
import br.com.nectar.domain.address.Address;
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

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeekeeperService {
    private final BeekeeperRepository beekeeperRepository;
    private final UserService userService;

    public Beekeeper create (
        CreateBeekeeperDTO createBeekeeper,
        User user
    ) {
        User org = userService.getUserOrg(user.getId());

        Beekeeper beekeeper = new Beekeeper();
        Profile profile = new Profile();

        beekeeper.setEmail(createBeekeeper.getEmail());
        beekeeper.setOwner(user);
        beekeeper.setOrg(org);

        profile.setName(createBeekeeper.getName());
        profile.setDocument(createBeekeeper.getDocument());
        profile.setPhone(createBeekeeper.getPhone());
        profile.setBirthDate(createBeekeeper.getBirthDate());

        if(createBeekeeper.getAddress() != null) {
            Address address = new Address();

            address.setStreet(createBeekeeper.getAddress().getStreet());
            address.setNumber(createBeekeeper.getAddress().getNumber());
            address.setCep(createBeekeeper.getAddress().getCep());
            address.setCity(createBeekeeper.getAddress().getCity());
            address.setState(createBeekeeper.getAddress().getState());
            address.setProvince(createBeekeeper.getAddress().getProvince());

            profile.setAddress(address);
        }

        beekeeper.setProfile(profile);

        return beekeeperRepository.save(beekeeper);
    }

    public Beekeeper update (
        UUID beekeeperid,
        CreateBeekeeperDTO createBeekeeper
    ) {
        Beekeeper beekeeper = getById(beekeeperid);
        Profile profile = beekeeper.getProfile();

        beekeeper.setStatus(createBeekeeper.getStatus());
        beekeeper.setEmail(createBeekeeper.getEmail());
        
        profile.setName(createBeekeeper.getName());
        profile.setDocument(createBeekeeper.getDocument());
        profile.setPhone(createBeekeeper.getPhone());
        profile.setBirthDate(createBeekeeper.getBirthDate());

        if(createBeekeeper.getAddress() != null) {
            Address address = profile.getAddress() != null
                    ? profile.getAddress()
                    : new Address();

            address.setStreet(createBeekeeper.getAddress().getStreet());
            address.setNumber(createBeekeeper.getAddress().getNumber());
            address.setCep(createBeekeeper.getAddress().getCep());
            address.setCity(createBeekeeper.getAddress().getCity());
            address.setState(createBeekeeper.getAddress().getState());
            address.setProvince(createBeekeeper.getAddress().getProvince());

            profile.setAddress(address);
        }

        beekeeper.setProfile(profile);

        return beekeeperRepository.save(beekeeper);
    }

    public Beekeeper getById (UUID id) {
        var beekeeper = beekeeperRepository.findById(id)
                .orElseThrow(() -> new FrontDisplayableException(
                    HttpStatus.BAD_REQUEST,
                    "Não foi possível encontrar o apicultor!"
                ));

        return beekeeper;
    }

    public Page<Beekeeper> getPage (
        User user,
        Integer page,
        GetPageDTO getPageDTO
    ) {
        User org = userService.getUserOrg(user.getId());

        return beekeeperRepository.getPageByStatus(
            org.getId(),
            UserStatus.ACTIVE,
            PageRequest.of(
                page,
                getPageDTO.getPageSize() > 0 ? getPageDTO.getPageSize() : 10
            )
        );
    }

    public List<Beekeeper> getAllActiveByUser (
        User user
    ) {
        User org = userService.getUserOrg(user.getId());

        return beekeeperRepository.getAllByStatus(
            org.getId(),
            UserStatus.ACTIVE
        );
    }

    public Long getQtdNewInMonth (
        User user,
        LocalDate month
    ) {
        User org = userService.getUserOrg(user.getId());

        var init = month.with(TemporalAdjusters.firstDayOfMonth());
        var end = month.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1);

        return beekeeperRepository.getQtdNewInMonth(
            org.getId(),
            init.atStartOfDay(),
            end.atTime(23, 59, 59),
            UserStatus.ACTIVE
        );
    }
}
