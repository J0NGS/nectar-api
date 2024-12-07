package br.com.nectar.domain.manager;

import br.com.nectar.application.beekepeer.dto.GetPageDTO;
import br.com.nectar.application.manager.dto.CreateManagerDTO;
import br.com.nectar.domain.address.Address;
import br.com.nectar.domain.auth.Auth;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final ManagerRepository managerRepository;
    private final UserService userService;

    public Manager create (
        CreateManagerDTO createForm,
        User user
    ) {
        User org = userService.getUserOrg(user.getId());

        Auth auth = new Auth();
        auth.setUsername(createForm.getEmail());
        auth.setPassword(createForm.getPassword());

        Profile profile = new Profile();
        profile.setName(createForm.getName());
        profile.setDocument(createForm.getDocument());
        profile.setPhone(createForm.getPhone());
        profile.setBirthDate(createForm.getBirthDate());

        if(createForm.getAddress() != null) {
            Address address = new Address();

            address.setStreet(createForm.getAddress().getStreet());
            address.setNumber(createForm.getAddress().getNumber());
            address.setCep(createForm.getAddress().getCep());
            address.setCity(createForm.getAddress().getCity());
            address.setState(createForm.getAddress().getState());
            address.setProvince(createForm.getAddress().getProvince());

            profile.setAddress(address);
        }

        User userManager = new User();
        userManager.setAuth(auth);
        userManager.setProfile(profile);

        Manager manager = new Manager();
        manager.setUser(userManager);
        manager.setOrg(org);

        return managerRepository.save(manager);
    }

    public Manager update (
        UUID managerId,
        CreateManagerDTO createForm
    ) {
        Manager manager = managerRepository.findById(managerId).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Gestor não encontrado!"
            )
        );

        User userManager = manager.getUser();
        userManager.setStatus(createForm.getStatus());

        Auth auth = userManager.getAuth();
        auth.setUsername(createForm.getEmail());
        auth.setPassword(createForm.getPassword());

        Profile profile = userManager.getProfile();
        profile.setName(createForm.getName());
        profile.setDocument(createForm.getDocument());
        profile.setPhone(createForm.getPhone());
        profile.setBirthDate(createForm.getBirthDate());

        if(createForm.getAddress() != null) {
            Address address = profile.getAddress() != null
                    ? profile.getAddress()
                    : new Address();

            address.setStreet(createForm.getAddress().getStreet());
            address.setNumber(createForm.getAddress().getNumber());
            address.setCep(createForm.getAddress().getCep());
            address.setCity(createForm.getAddress().getCity());
            address.setState(createForm.getAddress().getState());
            address.setProvince(createForm.getAddress().getProvince());

            profile.setAddress(address);
        }

        userManager.setAuth(auth);
        userManager.setProfile(profile);

        return managerRepository.save(manager);
    }

    public Manager getById (UUID id) {
        return managerRepository.findById(id)
            .orElseThrow(() -> new FrontDisplayableException(
                    HttpStatus.BAD_REQUEST,
                    "Não foi possível encontrar o gestor!"
            ));
    }

    public Page<Manager> getPage (
        User user,
        Integer page,
        GetPageDTO getPageDTO
    ) {
        User org = userService.getUserOrg(user.getId());

        return managerRepository.getPageByStatus(
            org.getId(),
            UserStatus.ACTIVE,
            PageRequest.of(
                page,
                getPageDTO.getPageSize() > 0 ? getPageDTO.getPageSize() : 10
            )
        );
    }

    public List<Manager> getAllActive (
        User user
    ) {
        User org = userService.getUserOrg(user.getId());

        return managerRepository.getAllByStatus(
            org.getId(),
            UserStatus.ACTIVE
        );
    }
}
