package br.com.oiinternet.poidnormalizer.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.oiinternet.oiapi.domain.MoiAssociation;

@Repository
public interface MoiAssociationRepository extends MongoRepository<MoiAssociation, ObjectId> {

    List<MoiAssociation> findByCpfCnpj(final String cpfCnpj);

    // @Query(value = "db.moiAssociation.aggregate( [ { $group: { _id: { cpfCnpj: “$cpfCnpj”}, count: { $sum: 1 } } }, {
    // $match: { count: { $gt: 1 } } }, { $count : “cpfCnpj” }] )")
    // List<Object[]> findDuplicatedMoiAssociation();

}
