package tn.esprit.spring.kaddem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.*;

@Slf4j
@Service
public class ContratServiceImpl implements IContratService {
	private final ContratRepository contratRepository;
	private final EtudiantRepository etudiantRepository;

	@Autowired
	public ContratServiceImpl(ContratRepository contratRepository, EtudiantRepository etudiantRepository) {
		this.contratRepository = contratRepository;
		this.etudiantRepository = etudiantRepository;
	}

	@Override
	public List<Contrat> retrieveAllContrats() {
		return contratRepository.findAll();
	}

	@Override
	public Contrat updateContrat(Contrat ce) {
		return contratRepository.findById(ce.getIdContrat())
				.map(existingContrat -> {
					existingContrat.setDateDebutContrat(ce.getDateDebutContrat());
					existingContrat.setDateFinContrat(ce.getDateFinContrat());
					existingContrat.setMontantContrat(ce.getMontantContrat());
					existingContrat.setSpecialite(ce.getSpecialite());
					existingContrat.setArchive(ce.getArchive());
					return contratRepository.save(existingContrat);
				}).orElse(null);
	}

	@Override
	public Contrat addContrat(Contrat ce) {
		return contratRepository.save(ce);
	}

	@Override
	public Contrat retrieveContrat(Integer idContrat) {
		return contratRepository.findById(idContrat).orElse(null);
	}

	@Override
	public void removeContrat(Integer idContrat) {
		contratRepository.findById(idContrat).ifPresent(contratRepository::delete);
	}

	@Override
	public Contrat affectContratToEtudiant(Integer idContrat, String nomE, String prenomE) {
		Etudiant e = etudiantRepository.findByNomEAndPrenomE(nomE, prenomE);
		Contrat ce = contratRepository.findByIdContrat(idContrat);
		long nbContratsActifs = e.getContrats().stream().filter(Contrat::getArchive).count();

		if (nbContratsActifs < 5) {
			ce.setEtudiant(e);
			contratRepository.save(ce);
		}

		return ce;
	}

	@Override
	public Integer nbContratsValides(Date startDate, Date endDate) {
		return contratRepository.getnbContratsValides(startDate, endDate);
	}

	@Override
	public void retrieveAndUpdateStatusContrat() {
		Date dateSysteme = new Date();
		List<Contrat> contrats = contratRepository.findAll();

		for (Contrat contrat : contrats) {
			if (!contrat.getArchive()) {
				long differenceInDays = (dateSysteme.getTime() - contrat.getDateFinContrat().getTime()) / (1000 * 60 * 60 * 24);

				if (differenceInDays == 15) {
					log.info("Contrat à échéance de 15 jours : {}", contrat);
				}

				if (differenceInDays == 0) {
					contrat.setArchive(true);
					contratRepository.save(contrat);
				}
			}
		}
	}

	@Override
	public float getChiffreAffaireEntreDeuxDates(Date startDate, Date endDate) {
		long differenceInDays = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
		float differenceInMonths = differenceInDays / 30f;

		Map<Specialite, Float> tarifs = Map.of(
				Specialite.IA, 300f,
				Specialite.CLOUD, 400f,
				Specialite.RESEAUX, 350f
		);

		return contratRepository.findAll().stream()
				.map(contrat -> differenceInMonths * tarifs.getOrDefault(contrat.getSpecialite(), 450f))
				.reduce(0f, Float::sum);
	}

	@Override
	public int getNbContratsValides() {
		return contratRepository.countByValide(true);
	}
}
