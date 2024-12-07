package br.com.nectar.domain.beekeeper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BeekeeperService {
    private final BeekeeperRepository beekeeperRepository;
}
