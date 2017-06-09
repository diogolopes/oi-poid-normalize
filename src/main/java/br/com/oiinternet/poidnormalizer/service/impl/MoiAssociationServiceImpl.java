package br.com.oiinternet.poidnormalizer.service.impl;

import static br.com.oi.oicommons.brm.util.BRMUtils.hasVersion;
import static br.com.oi.oicommons.brm.util.BRMUtils.removeVersion;
import static org.springframework.util.StringUtils.isEmpty;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.com.oiinternet.oiapi.domain.MoiAssociation;
import br.com.oiinternet.oiapi.sdk.enumeration.Status;
import br.com.oiinternet.oiapi.sdk.enumeration.SystemSource;
import br.com.oiinternet.poidnormalizer.Application;
import br.com.oiinternet.poidnormalizer.repository.MoiAssociationRepository;
import br.com.oiinternet.poidnormalizer.service.MoiAssociationService;

@Service
public class MoiAssociationServiceImpl implements MoiAssociationService {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Application.class);

    private static final String POID_SEPARATOR = " ";
    private static final String TVOD_EMAIL = "user@tvod.oi.br";

    private static final int MAX_PER_PAGE = 100;

    private AtomicInteger amount = new AtomicInteger(0);

    private final MoiAssociationRepository moiAssociationRepository;

    public MoiAssociationServiceImpl(MoiAssociationRepository moiAssociationRepository) {
        this.moiAssociationRepository = moiAssociationRepository;
    }

    @Override
    public AtomicInteger findDuplicatedMoiAssociation() {

        long total = moiAssociationRepository.count();
        int totalOfPages = Long.valueOf((total / MAX_PER_PAGE) + 1).intValue();

        for (int i = 0; i < totalOfPages; i++) {
            final Page<MoiAssociation> pageResult = moiAssociationRepository.findAll(new PageRequest(i, MAX_PER_PAGE));

            // substituir pela query de aggregate o resultado ja com count > 1
            final Map<String, Long> counting = pageResult.getContent().stream()
                    .collect(Collectors.groupingBy(MoiAssociation::getCpfCnpj, Collectors.counting()));

            final Set<String> duplicatedCpfCnpj = counting.entrySet().stream()
                    .flatMap(entry -> Stream.of(entry.getKey())).collect(Collectors.toSet());

            duplicatedCpfCnpj.forEach(cpfCnpj -> fixMoiAssociationStatus(cpfCnpj));

        }

        return amount;
    }

    @Override
    public void normalizePoid(final MoiAssociation moiAssociation) {
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

    private void fixMoiAssociationStatus(final String cpfCnpj) {
        final List<MoiAssociation> moiAssociationList = moiAssociationRepository.findByCpfCnpj(cpfCnpj);
        if (CollectionUtils.isEmpty(moiAssociationList) || moiAssociationList.size() < 2) {
            return;
        }

        amount.incrementAndGet();

        Date currentAssociationDate = null;
        int activeStatusCount = 0;

        // 1o - Se algum dos registros tiver o status ativo, muda os outros para inativo.
        for (final MoiAssociation moiAssociation : moiAssociationList) {
            final Status status = moiAssociation.getStatus();
            final Date associationDate = moiAssociation.getAssociationDate();

            if (status != null && status.equals(Status.ACTIVE)) {
                activeStatusCount++;
            }

            if (currentAssociationDate == null || associationDate.after(currentAssociationDate)) {
                currentAssociationDate = moiAssociation.getAssociationDate();
            }
        }

        // Só tem um moiAssociation Ativo, mudar os outros para inativo.
        if (activeStatusCount == 1) {
            for (final MoiAssociation moiAssociation : moiAssociationList) {
                final Status status = moiAssociation.getStatus();

                if (status == null) {
                    moiAssociation.setStatus(Status.INACTIVE);
                    moiAssociationRepository.save(moiAssociation);
                    LOGGER.info("Already has an ACTIVE status. Set stastus to INACTIVE for cpfCnpj {}, poid {}",
                            moiAssociation.getCpfCnpj(), moiAssociation.getAccountPoid());
                } else {
                    LOGGER.info("Already has an status defined. Nothing to do for cpfCnpj {}, poid {}",
                            moiAssociation.getCpfCnpj(), moiAssociation.getAccountPoid());
                }
            }
        } else {

            for (final MoiAssociation moiAssociation : moiAssociationList) {

                if (moiAssociation.getAssociationDate().before(currentAssociationDate)) {
                    moiAssociation.setStatus(Status.INACTIVE);
                    moiAssociationRepository.save(moiAssociation);
                    LOGGER.info("Exists a current association. Set stastus to INACTIVE for cpfCnpj {}, poid {}",
                            moiAssociation.getCpfCnpj(), moiAssociation.getAccountPoid());

                } else {
                    moiAssociation.setStatus(Status.ACTIVE);
                    moiAssociationRepository.save(moiAssociation);
                    LOGGER.info("This is most current association. Set stastus to ACTIVE for cpfCnpj {}, poid {}",
                            moiAssociation.getCpfCnpj(), moiAssociation.getAccountPoid());
                }
            }
        }

    }

}
