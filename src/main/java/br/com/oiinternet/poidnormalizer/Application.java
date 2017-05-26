package br.com.oiinternet.poidnormalizer;

import static br.com.oi.oicommons.brm.util.BRMUtils.hasVersion;
import static br.com.oi.oicommons.brm.util.BRMUtils.removeVersion;
import static org.springframework.util.StringUtils.isEmpty;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.oiinternet.oiapi.sdk.enumeration.SystemSource;
import br.com.oiinternet.poidnormalizer.domain.MoiAssociation;
import br.com.oiinternet.poidnormalizer.repository.MoiAssociationRepository;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Application.class);

    private static final String POID_SEPARATOR = " ";
    private static final String TVOD_EMAIL = "user@tvod.oi.br";

    private final MoiAssociationRepository moiAssociationRepository;

    public Application(MoiAssociationRepository moiAssociationRepository) {
        this.moiAssociationRepository = moiAssociationRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        final List<MoiAssociation> findAll = moiAssociationRepository.findAll();
        LOGGER.info("Found {} moiAssociations", findAll.size());
        findAll.forEach(m -> normalizePoid(m));

    }

    private void normalizePoid(final MoiAssociation moiAssociation) {
        boolean hasChanges = false;
        if (hasVersion(moiAssociation.getAccountPoid(), POID_SEPARATOR)) {
            moiAssociation.setAccountPoid(removeVersion(moiAssociation.getAccountPoid(), POID_SEPARATOR));
            hasChanges = true;
        }

        if (isEmpty(moiAssociation.getSystemSource())) {
            if (!isEmpty(moiAssociation.getEmail()) && moiAssociation.getEmail().equals(TVOD_EMAIL)) {
                moiAssociation.setSystemSource(SystemSource.ONLINE);
            } else {
                moiAssociation.setSystemSource(SystemSource.PARTNER_API);
            }
            hasChanges = true;
        }

        if (hasChanges) {
            final MoiAssociation savedMoiAssociation = moiAssociationRepository.save(moiAssociation);
            LOGGER.info("Saved {} moiAssociation", savedMoiAssociation);
        } else {
            LOGGER.info("Keep {} moiAssociation", moiAssociation);
        }
    }

}