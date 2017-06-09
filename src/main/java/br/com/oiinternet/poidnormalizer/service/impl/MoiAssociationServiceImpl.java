package br.com.oiinternet.poidnormalizer.service.impl;

import static br.com.oi.oicommons.brm.util.BRMUtils.hasVersion;
import static br.com.oi.oicommons.brm.util.BRMUtils.removeVersion;
import static org.springframework.util.StringUtils.isEmpty;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    private static final int MAX_PER_PAGE = 1000;

    private final AtomicInteger statusEmptyAmount = new AtomicInteger(0);
    private final Set<String> updatedCnpj = new HashSet<>();

    private final MoiAssociationRepository moiAssociationRepository;

    public MoiAssociationServiceImpl(MoiAssociationRepository moiAssociationRepository) {
        this.moiAssociationRepository = moiAssociationRepository;
    }

    @Override
    public int findDuplicatedMoiAssociation() {

        long total = moiAssociationRepository.count();
        int totalOfPages = Long.valueOf((total / MAX_PER_PAGE) + 1).intValue();

        for (int i = 0; i < totalOfPages; i++) {
            LOGGER.info("Page {} of {}", i, totalOfPages);

            final Page<MoiAssociation> pageResult = moiAssociationRepository.findAll(new PageRequest(i, MAX_PER_PAGE));

            pageResult.getContent().stream().map(MoiAssociation::getCpfCnpj).collect(Collectors.toSet())
                    .forEach(cpf -> fixMoiAssociationStatus(cpf));
        }

        final List<MoiAssociation> moiAssociationWithoutStatus = moiAssociationRepository.findByStatusIsNull();
        final int statusEmptySize = moiAssociationWithoutStatus.size();
        LOGGER.info("Found {} with null status", statusEmptySize);
        moiAssociationWithoutStatus.forEach(moiAssociation -> setActiveStatusMoiAssociationStatus(moiAssociation));
        LOGGER.info("Updated {} of {} with null status", statusEmptyAmount.get(), statusEmptySize);

        return updatedCnpj.size();
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

    private void setActiveStatusMoiAssociationStatus(final MoiAssociation moiAssociation) {
        if (moiAssociation.getStatus() == null) {
            statusEmptyAmount.incrementAndGet();
            moiAssociation.setStatus(Status.ACTIVE);
            moiAssociationRepository.save(moiAssociation);
            LOGGER.info("MoiAssociation with null status. Change to ACTIVE for cpfCnpj {}, poid {}",
                    moiAssociation.getCpfCnpj(), moiAssociation.getAccountPoid());
        }
    }

    private void fixMoiAssociationStatus(final String cpfCnpj) {
        final List<MoiAssociation> moiAssociationList = moiAssociationRepository.findByCpfCnpj(cpfCnpj);

        if (CollectionUtils.isEmpty(moiAssociationList) || moiAssociationList.size() < 2
                || updatedCnpj.contains(cpfCnpj)) {
            return;
        }

        updatedCnpj.add(cpfCnpj);

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
