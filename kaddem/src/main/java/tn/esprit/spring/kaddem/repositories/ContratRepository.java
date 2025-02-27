package tn.esprit.spring.kaddem.repositories;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.kaddem.entities.Contrat;

import java.util.Date;
import java.util.List;


@Repository
public interface ContratRepository extends CrudRepository<Contrat, Integer> {

    @Query("SELECT count(c) FROM Contrat c where ((c.archive=true) and  ((c.dateDebutContrat BETWEEN :startDate AND :endDate)) or(c.dateFinContrat BETWEEN :startDate AND :endDate))")
public Integer getnbContratsValides(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

public List<Contrat> findAll();
public Contrat findByIdContrat(Integer idContrat);

    int countByValide(boolean b);
}
