package tn.esprit.spring.kaddem.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Slf4j
@AllArgsConstructor
@Service
public class EquipeServiceImpl implements IEquipeService{
	EquipeRepository equipeRepository;


	public List<Equipe> retrieveAllEquipes(){
	return  (List<Equipe>) equipeRepository.findAll();
	}
	public Equipe addEquipe(Equipe e){
		return (equipeRepository.save(e));
	}

	public  void deleteEquipe(Integer idEquipe){
		Equipe e=retrieveEquipe(idEquipe);
		equipeRepository.delete(e);
	}

	public Equipe retrieveEquipe(Integer equipeId) {
		Optional<Equipe> equipeOptional = equipeRepository.findById(equipeId);
		if (equipeOptional.isPresent()) {
			return equipeOptional.get();
		} else {
			// You can return null, throw an exception, or handle it as needed.
			// For example, throwing an exception:
			throw new EntityNotFoundException("Equipe with id " + equipeId + " not found");
		}
	}

	public Equipe updateEquipe(Equipe e){
	return (	equipeRepository.save(e));
	}

	public void evoluerEquipes() {
		List<Equipe> equipes = new ArrayList<>();
		equipeRepository.findAll().forEach(equipes::add);


		for (Equipe equipe : equipes) {
			if (equipe.getNiveau() == Niveau.JUNIOR || equipe.getNiveau() == Niveau.SENIOR) {
				long nbEtudiantsAvecContratsActifs = equipe.getEtudiants().stream()
						.filter(this::aContratActifDepuisPlusDunAn)
						.limit(3) // On arrête si on atteint 3 étudiants
						.count();

				if (nbEtudiantsAvecContratsActifs >= 3) {
					evoluerNiveauEquipe(equipe);
				}
			}
		}
	}

	private boolean aContratActifDepuisPlusDunAn(Etudiant etudiant) {
		Date dateActuelle = new Date();
		return etudiant.getContrats().stream()
				.anyMatch(contrat -> !contrat.getArchive() &&
						dateActuelle.getTime() - contrat.getDateFinContrat().getTime() > 365L * 24 * 60 * 60 * 1000);
	}

	private void evoluerNiveauEquipe(Equipe equipe) {
		if (equipe.getNiveau() == Niveau.JUNIOR) {
			equipe.setNiveau(Niveau.SENIOR);
		} else if (equipe.getNiveau() == Niveau.SENIOR) {
			equipe.setNiveau(Niveau.EXPERT);
		}
		equipeRepository.save(equipe);
	}

}