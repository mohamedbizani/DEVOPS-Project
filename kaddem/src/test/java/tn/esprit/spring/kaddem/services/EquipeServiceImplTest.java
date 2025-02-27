package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.kaddem.entities.*;
import tn.esprit.spring.kaddem.repositories.*;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class EquipeServiceImplTest {


    @Mock
    private EquipeRepository equipeRepository;
    @Mock
    private ContratRepository contratRepository;
    @Mock
    private EtudiantRepository etudiantRepository;
    @Mock
    private DepartementRepository departementRepository;

    @InjectMocks
    private EquipeServiceImpl equipeServiceImpl;


    @BeforeEach
    void setUp() {
        // Initialisation des mocks avec @MockitoAnnotations.initMocks() supprimée
    }

    @Test
    void testRetrieveAllEquipes() {
        // Préparer les données
        List<Equipe> equipes = new ArrayList<>();
        equipes.add(new Equipe("Bouzid", Niveau.EXPERT));
        equipes.add(new Equipe("haythem", Niveau.EXPERT));

        // Simuler la méthode du repository
        when(equipeRepository.findAll()).thenReturn(equipes);

        // Appeler la méthode à tester
        List<Equipe> result = equipeServiceImpl.retrieveAllEquipes();

        // Vérifier le résultat
        assertEquals(2, result.size());
        verify(equipeRepository, times(1)).findAll();
    }

    @Test
    void testAddEquipe() {
        // Créer un exemple d'équipe
        Equipe equipe = new Equipe("TestNom", Niveau.EXPERT);

        // Simuler le comportement du repository
        when(equipeRepository.save(equipe)).thenReturn(equipe);

        // Appeler la méthode à tester
        Equipe result = equipeServiceImpl.addEquipe(equipe);

        // Vérifier le résultat
        assertEquals("TestNom", result.getNomEquipe());
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    void testUpdateEquipe() {
        // Créer un exemple d'équipe
        Equipe equipe = new Equipe("TestNom", Niveau.EXPERT);
        equipe.setIdEquipe(1);

        // Simuler le comportement du repository
        when(equipeRepository.save(equipe)).thenReturn(equipe);

        // Appeler la méthode à tester
        Equipe result = equipeServiceImpl.updateEquipe(equipe);

        // Vérifier le résultat
        assertEquals(1, result.getIdEquipe());
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    void testRetrieveEquipe() {
        // Créer un exemple d'équipe
        Equipe equipe = new Equipe("TestNom", Niveau.EXPERT);
        equipe.setIdEquipe(1);

        // Simuler la méthode du repository
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));

        // Appeler la méthode à tester
        Equipe result = equipeServiceImpl.retrieveEquipe(1);

        // Vérifier le résultat
        assertNotNull(result);
        assertEquals(1, result.getIdEquipe());
        verify(equipeRepository, times(1)).findById(1);
    }

    @Test
    void testRemoveEquipe() {
        // Créer un exemple d'équipe
        Equipe equipe = new Equipe("TestNom", Niveau.EXPERT);
        equipe.setIdEquipe(1);

        // Simuler la méthode du repository
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));

        // Appeler la méthode à tester
        equipeServiceImpl.deleteEquipe(1);

        // Vérifier les interactions
        verify(equipeRepository, times(1)).delete(equipe);
    }
}
