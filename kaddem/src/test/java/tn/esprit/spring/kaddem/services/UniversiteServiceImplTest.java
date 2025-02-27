package tn.esprit.spring.kaddem.services;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;
import tn.esprit.spring.kaddem.services.UniversiteServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UniversiteServiceImplTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private UniversiteServiceImpl universiteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUniversite() {
        // Créer une université
        Universite universite = new Universite("Test University");

        // Simuler le comportement du repository
        when(universiteRepository.save(universite)).thenReturn(universite);

        // Appeler la méthode à tester
        Universite result = universiteService.addUniversite(universite);

        // Vérifier le résultat
        assertEquals("Test University", result.getNomUniv());
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    void testRetrieveUniversite() {
        // Créer une université
        Universite universite = new Universite(1, "Test University");

        // Simuler la méthode du repository
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));

        // Appeler la méthode à tester
        Universite result = universiteService.retrieveUniversite(1);

        // Vérifier le résultat
        assertNotNull(result);
        assertEquals(1, result.getIdUniv());
        verify(universiteRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateUniversite() {
        // Créer une université
        Universite updatedUniversite = new Universite(1, "Updated Name");

        // Simuler le comportement du repository
        when(universiteRepository.save(updatedUniversite)).thenReturn(updatedUniversite);

        // Appeler la méthode à tester
        Universite result = universiteService.updateUniversite(updatedUniversite);

        // Vérifier le résultat
        assertEquals("Updated Name", result.getNomUniv());
        verify(universiteRepository, times(1)).save(updatedUniversite);
    }

    @Test
    void testDeleteUniversite() {
        // Créer une université
        Universite universite = new Universite(1, "Test University");

        // Simuler la méthode du repository
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));

        // Appeler la méthode à tester
        universiteService.deleteUniversite(1);

        // Vérifier les interactions
        verify(universiteRepository, times(1)).delete(universite);
    }

    @Test
    void testRetrieveAllUniversites() {
        // Création de quelques universités fictives
        Universite uni1 = new Universite(1, "Université A");
        Universite uni2 = new Universite(2, "Université B");
        Universite uni3 = new Universite(3, "Université C");

        // Simulation du comportement du repository pour retourner une liste d'universités
        List<Universite> universites = Arrays.asList(uni1, uni2, uni3);
        when(universiteRepository.findAll()).thenReturn(universites);

        // Appel de la méthode testée
        List<Universite> result = universiteService.retrieveAllUniversites();

        // Vérifications
        assertNotNull(result); // La liste ne doit pas être nulle
        assertEquals(3, result.size()); // Il y a 3 universités
        assertEquals("Université A", result.get(0).getNomUniv()); // Vérification de la première université
        assertEquals("Université B", result.get(1).getNomUniv()); // Vérification de la deuxième université
        assertEquals("Université C", result.get(2).getNomUniv()); // Vérification de la troisième université

        // Vérifier que findAll() a bien été appelé une seule fois
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveDepartementsByUniversite_Success() {
        // Création d'un ensemble de départements fictifs
        Departement dep1 = new Departement(1, "Informatique");
        Departement dep2 = new Departement(2, "Mathématiques");
        Set<Departement> departements = new HashSet<>();
        departements.add(dep1);
        departements.add(dep2);

        // Création d'une université contenant ces départements
        Universite universite = new Universite(1, "Université A", departements);

        // Simulation du repository
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));

        // Exécution de la méthode testée
        Set<Departement> result = universiteService.retrieveDepartementsByUniversite(1);

        // Vérification des résultats
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(dep1));
        assertTrue(result.contains(dep2));
    }

    @Test
    void testRetrieveDepartementsByUniversite_NotFound() {
        // Simulation d'une université inexistante
        when(universiteRepository.findById(1)).thenReturn(Optional.empty());

        // Exécution de la méthode testée
        Set<Departement> result = universiteService.retrieveDepartementsByUniversite(1);

        // Vérification que la méthode retourne null
        assertNull(result);
    }

}
