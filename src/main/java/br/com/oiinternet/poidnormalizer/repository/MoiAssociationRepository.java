package br.com.oiinternet.poidnormalizer.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.oiinternet.poidnormalizer.domain.MoiAssociation;

@Repository
public interface MoiAssociationRepository extends MongoRepository<MoiAssociation, ObjectId> {

}
