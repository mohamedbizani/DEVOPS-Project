package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.ContratServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContratServiceImplTest {

    @Mock
    private ContratRepository contratRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private ContratServiceImpl contratService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllContrats() {
        // Initialize test data
        List<Contrat> contrats = new ArrayList<>();
        contrats.add(new Contrat(new Date(), new Date(), null, false, 1000));
        contrats.add(new Contrat(new Date(), new Date(), null, false, 1500));

        when(contratRepository.findAll()).thenReturn(contrats);

        List<Contrat> result = contratService.retrieveAllContrats();

        assertEquals(2, result.size());
        verify(contratRepository, times(1)).findAll();
    }

    @Test
    void testAddContrat() {
        // Initialize test data
        Contrat contrat = new Contrat(new Date(), new Date(), null, false, 1000);
        when(contratRepository.save(contrat)).thenReturn(contrat);

        Contrat result = contratService.addContrat(contrat);

        assertNotNull(result);
        assertEquals(1000, result.getMontantContrat());
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    void testRetrieveContrat() {
        // Initialize test data
        Integer contratId = 1;
        Contrat contrat = new Contrat(new Date(), new Date(), null, false, 1000);
        contrat.setIdContrat(contratId);

        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));

        Contrat result = contratService.retrieveContrat(contratId);

        assertNotNull(result);
        assertEquals(contratId, result.getIdContrat());
        verify(contratRepository, times(1)).findById(contratId);
    }

    @Test
    void testRemoveContrat() {
        // Initialize test data
        Integer contratId = 1;
        Contrat contrat = new Contrat(new Date(), new Date(), null, false, 1000);
        contrat.setIdContrat(contratId);

        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));

        contratService.removeContrat(contratId);

        verify(contratRepository, times(1)).delete(contrat);
    }

    @Test
    void testAffectContratToEtudiant() {
        Integer contratId = 1;
        String nomE = "Nom";
        String prenomE = "Prenom";
        Contrat contrat = new Contrat(new Date(), new Date(), null, false, 1000);
        contrat.setIdContrat(contratId);

        when(etudiantRepository.findByNomEAndPrenomE(nomE, prenomE)).thenReturn(new Etudiant());
        when(contratRepository.findByIdContrat(contratId)).thenReturn(contrat);

        Contrat result = contratService.affectContratToEtudiant(contratId, nomE, prenomE);

        assertNotNull(result);
        verify(etudiantRepository, times(1)).findByNomEAndPrenomE(nomE, prenomE);
        verify(contratRepository, times(1)).findByIdContrat(contratId);
        verify(contratRepository, times(1)).save(contrat);
    }
    @Test
    void testUpdateContrat() {
        // Initialize test data
        Integer contratId = 1;
        Contrat existingContrat = new Contrat(new Date(), new Date(), null, false, 1000);
        existingContrat.setIdContrat(contratId);

        // Mock behavior
        when(contratRepository.findById(contratId)).thenReturn(Optional.of(existingContrat));
        when(contratRepository.save(any(Contrat.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Update fields
        existingContrat.setMontantContrat(2000);
        existingContrat.setArchive(true);

        // Call service method
        Contrat result = contratService.updateContrat(existingContrat);

        // Assertions
        assertNotNull(result);
        assertEquals(2000, result.getMontantContrat());
        assertTrue(result.getArchive());

        // Verify interactions
        verify(contratRepository, times(1)).findById(contratId);
        verify(contratRepository, times(1)).save(existingContrat);
    }

    @Test
    void testGetNbContratsValides() {
        // Initialize test data
        List<Contrat> contrats = new ArrayList<>();
        contrats.add(new Contrat(new Date(), new Date(), null, false, 1000));
        contrats.add(new Contrat(new Date(), new Date(), null, true, 1500));
        contrats.add(new Contrat(new Date(), new Date(), null, false, 2000));

        when(contratRepository.findAll()).thenReturn(contrats);

        int result = contratService.getNbContratsValides();

        assertEquals(1, result);  // Only one contrat is valid (not archived)
        verify(contratRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveAndUpdateStatusContrat() {
        // Création de contrats avec des dates spécifiques
        Contrat contratNonArchive = new Contrat();
        contratNonArchive.setArchive(false);
        contratNonArchive.setDateFinContrat(getDateNDaysAgo(15)); // Expire dans 15 jours

        Contrat contratAarchiver = new Contrat();
        contratAarchiver.setArchive(false);
        contratAarchiver.setDateFinContrat(new Date()); // Expire aujourd'hui

        // Mock du repository pour renvoyer les contrats simulés
        when(contratRepository.findAll()).thenReturn(Arrays.asList(contratNonArchive, contratAarchiver));

        // Exécution de la méthode
        contratService.retrieveAndUpdateStatusContrat();

        // Vérifications
        assertTrue(contratAarchiver.getArchive(), "Le contrat qui expire aujourd'hui doit être archivé.");
        verify(contratRepository, times(1)).save(contratAarchiver); // Vérifie que save() est bien appelé une fois
    }

    // Méthode utilitaire pour obtenir une date en fonction du nombre de jours
    private Date getDateNDaysAgo(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -days);
        return calendar.getTime();
    }
    @Test
    void testGetChiffreAffaireEntreDeuxDates() {
        // Définir les dates de début et de fin
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.JANUARY, 1);
        Date startDate = cal.getTime();

        cal.set(2024, Calendar.MARCH, 1);
        Date endDate = cal.getTime();

        // Liste de contrats fictifs
        List<Contrat> contrats = Arrays.asList(
                new Contrat(Specialite.IA),
                new Contrat(Specialite.CLOUD),
                new Contrat(Specialite.RESEAUX),
                new Contrat(Specialite.SECURITE)
        );

        // Simulation du comportement du repository
        when(contratRepository.findAll()).thenReturn(contrats);

        // Appeler la méthode testée
        float result = contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        // Vérification du résultat attendu
        float expected = (2 * 300) + (2 * 400) + (2 * 350) + (2 * 450);
        assertEquals(expected, result, 0.01);
    }
}


