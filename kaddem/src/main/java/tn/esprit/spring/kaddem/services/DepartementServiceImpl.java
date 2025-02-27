package tn.esprit.spring.kaddem.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class DepartementServiceImpl implements IDepartementService {

	private final DepartementRepository departementRepository;

	// Constructor injection
	public DepartementServiceImpl(DepartementRepository departementRepository) {
		this.departementRepository = departementRepository;
	}

	public List<Departement> retrieveAllDepartements() {
		return (List<Departement>) departementRepository.findAll();
	}

	public Departement addDepartement(Departement d) {
		return departementRepository.save(d);
	}

	public Departement updateDepartement(Departement d) {
		return departementRepository.save(d);
	}

	public Departement retrieveDepartement(Integer idDepart) {
		return departementRepository.findById(idDepart)
				.orElseThrow(() -> new NoSuchElementException("Departement not found with id: " + idDepart));
	}



	public void deleteDepartement(Integer idDepartement) {
		Departement d = retrieveDepartement(idDepartement);
		departementRepository.delete(d);
	}
}
