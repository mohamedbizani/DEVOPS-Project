package tn.esprit.spring.kaddem.services;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
public class EtudiantServiceImpl implements IEtudiantService {

	private final EtudiantRepository etudiantRepository;
	private final ContratRepository contratRepository;
	private final EquipeRepository equipeRepository;
	private final DepartementRepository departementRepository;

	// Constructor injection
	public EtudiantServiceImpl(EtudiantRepository etudiantRepository, ContratRepository contratRepository,
							   EquipeRepository equipeRepository, DepartementRepository departementRepository) {
		this.etudiantRepository = etudiantRepository;
		this.contratRepository = contratRepository;
		this.equipeRepository = equipeRepository;
		this.departementRepository = departementRepository;
	}

	public List<Etudiant> retrieveAllEtudiants() {
		return (List<Etudiant>) etudiantRepository.findAll();
	}

	public Etudiant addEtudiant(Etudiant e) {
		return etudiantRepository.save(e);
	}

	public Etudiant updateEtudiant(Etudiant e) {
		return etudiantRepository.save(e);
	}

	public Etudiant retrieveEtudiant(Integer idEtudiant) {
		return etudiantRepository.findById(idEtudiant).orElse(null);
	}

	public void removeEtudiant(Integer idEtudiant) {
		Etudiant e = retrieveEtudiant(idEtudiant);
		etudiantRepository.delete(e);
	}

	public void assignEtudiantToDepartement(Integer etudiantId, Integer departementId) {
		Etudiant etudiant = etudiantRepository.findById(etudiantId).orElse(null);
		Departement departement = departementRepository.findById(departementId).orElse(null);
		if (etudiant != null && departement != null) {
			etudiant.setDepartement(departement);
			etudiantRepository.save(etudiant);
		}
	}

	@Transactional
	public Etudiant addAndAssignEtudiantToEquipeAndContract(Etudiant e, Integer idContrat, Integer idEquipe) {
		Contrat c = contratRepository.findById(idContrat).orElse(null);
		Equipe eq = equipeRepository.findById(idEquipe).orElse(null);

		if (c != null && eq != null) {
			c.setEtudiant(e);
			// Si eq.getEtudiants() est nul, initialise-le
			if (eq.getEtudiants() == null) {
				eq.setEtudiants(new HashSet<>());
			}
			eq.getEtudiants().add(e);  // Ajoute l'étudiant à l'équipe

			// Sauvegarder l'étudiant
			etudiantRepository.save(e);
		}
		return e;
	}



	public List<Etudiant> getEtudiantsByDepartement(Integer idDepartement) {
		return etudiantRepository.findEtudiantsByDepartement_IdDepart(idDepartement);
	}
}
