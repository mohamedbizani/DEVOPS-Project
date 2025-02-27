package tn.esprit.spring.kaddem.services;

import org.springframework.stereotype.Service;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;

import java.util.List;
import java.util.Set;

@Service
public class UniversiteServiceImpl implements IUniversiteService {

    private final UniversiteRepository universiteRepository;
    private final DepartementRepository departementRepository;

    // Constructor injection
    public UniversiteServiceImpl(UniversiteRepository universiteRepository, DepartementRepository departementRepository) {
        this.universiteRepository = universiteRepository;
        this.departementRepository = departementRepository;
    }

    @Override
    public List<Universite> retrieveAllUniversites() {
        return (List<Universite>) universiteRepository.findAll();
    }

    @Override
    public Universite addUniversite(Universite u) {
        return universiteRepository.save(u);
    }

    @Override
    public Universite updateUniversite(Universite u) {
        return universiteRepository.save(u);
    }

    @Override
    public Universite retrieveUniversite(Integer idUniversite) {
        return universiteRepository.findById(idUniversite).orElse(null); // Return null if not found
    }

    @Override
    public void deleteUniversite(Integer idUniversite) {
        universiteRepository.delete(retrieveUniversite(idUniversite));
    }

    @Override
    public void assignUniversiteToDepartement(Integer idUniversite, Integer idDepartement) {
        Universite u = universiteRepository.findById(idUniversite).orElse(null);
        Departement d = departementRepository.findById(idDepartement).orElse(null);
        if (u != null && d != null) {
            u.getDepartements().add(d);
            universiteRepository.save(u);
        }
    }

    @Override
    public Set<Departement> retrieveDepartementsByUniversite(Integer idUniversite) {
        return universiteRepository.findById(idUniversite)
                .map(Universite::getDepartements)
                .orElse(null);
    }

}
