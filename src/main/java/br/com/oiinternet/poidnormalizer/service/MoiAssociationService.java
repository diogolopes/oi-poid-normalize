package br.com.oiinternet.poidnormalizer.service;

import java.util.concurrent.atomic.AtomicInteger;

import br.com.oiinternet.oiapi.domain.MoiAssociation;

public interface MoiAssociationService {

    void normalizePoid(final MoiAssociation moiAssociation);

    AtomicInteger findDuplicatedMoiAssociation();

}
