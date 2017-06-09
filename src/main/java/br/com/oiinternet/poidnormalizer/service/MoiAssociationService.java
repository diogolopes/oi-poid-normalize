package br.com.oiinternet.poidnormalizer.service;

import br.com.oiinternet.oiapi.domain.MoiAssociation;

public interface MoiAssociationService {

    void normalizePoid(final MoiAssociation moiAssociation);

    int findDuplicatedMoiAssociation();

}
