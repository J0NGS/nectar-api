package br.com.nectar.domain.beekeeper;

import br.com.nectar.application.beekepeer.dto.CreateBeekeeperDTO;
import br.com.nectar.domain.address.Address;
import br.com.nectar.domain.profile.Profile;
import br.com.nectar.infrastructure.exceptions.FrontDisplayableException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeekeeperService {
    private final BeekeeperRepository beekeeperRepository;

    public Beekeeper create (CreateBeekeeperDTO createBeekeeper) {
        Beekeeper beekeeper = new Beekeeper();
        Profile profile = new Profile();
        Address address = new Address();

        beekeeper.setEmail(createBeekeeper.getEmail());

        profile.setName(createBeekeeper.getName());
        profile.setDocument(createBeekeeper.getDocument());
        profile.setPhone(createBeekeeper.getPhone());
        profile.setBirthDate(createBeekeeper.getBirthDate());

        address.setStreet(createBeekeeper.getStreet());
        address.setNumber(createBeekeeper.getNumber());
        address.setCep(createBeekeeper.getCep());
        address.setCity(createBeekeeper.getCity());
        address.setState(createBeekeeper.getState());
        address.setProvince(createBeekeeper.getProvince());

        profile.setAddress(address);
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
}
