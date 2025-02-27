package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;


import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepartementServiceTest {

   @InjectMocks
   private DepartementServiceImpl departementService;

   @Mock
   private DepartementRepository departementRepository;

   @BeforeEach
   void init() {
      MockitoAnnotations.openMocks(this);
   }

   @Test
   void testCreateDepartement() {
      Departement departement = new Departement("Informatique");

      when(departementRepository.save(departement)).thenReturn(departement);

      Departement savedDepartement = departementService.addDepartement(departement);

      assertNotNull(savedDepartement);
      assertEquals("Informatique", savedDepartement.getNomDepart());
      verify(departementRepository).save(departement);
   }

   @Test
   void testDeleteDepartement() {
      Departement departement = new Departement(1, "Informatique");

      when(departementRepository.findById(1)).thenReturn(Optional.of(departement));

      departementService.deleteDepartement(1);

      verify(departementRepository).findById(1);
      verify(departementRepository).delete(departement);
   }

   @Test
   void testUpdateDepartement() {
      Departement departement = new Departement(1, "Informatique");
      departement.setNomDepart("Engineering");

      when(departementRepository.save(departement)).thenReturn(departement);

      Departement updatedDepartement = departementService.updateDepartement(departement);

      assertNotNull(updatedDepartement);
      assertEquals("Engineering", updatedDepartement.getNomDepart());
      verify(departementRepository).save(departement);
   }
   @Test
   void testRetrieveAllDepartements() {
      // Création de départements fictifs
      Departement dep1 = new Departement(1, "Informatique");
      Departement dep2 = new Departement(2, "Mathématiques");
      List<Departement> departements = Arrays.asList(dep1, dep2);

      // Simulation du repository
      when(departementRepository.findAll()).thenReturn(departements);

      // Appel de la méthode testée
      List<Departement> result = departementService.retrieveAllDepartements();

      // Vérifications
      assertNotNull(result);
      assertEquals(2, result.size());
      assertEquals("Informatique", result.get(0).getNomDepart());
      assertEquals("Mathématiques", result.get(1).getNomDepart());

      // Vérifier que findAll() a été appelé exactement une fois
      verify(departementRepository, times(1)).findAll();
   }
   @Test
   void testRetrieveDepartement_Success() {
      // ID fictif
      Integer id = 1;
      Departement dep = new Departement(id, "Informatique");

      // Simulation du comportement du repository
      when(departementRepository.findById(id)).thenReturn(Optional.of(dep));

      // Appel de la méthode testée
      Departement result = departementService.retrieveDepartement(id);

      // Vérifications
      assertNotNull(result);
      assertEquals(id, result.getIdDepart());
      assertEquals("Informatique", result.getNomDepart());

      // Vérifier que findById() a été appelé une seule fois
      verify(departementRepository, times(1)).findById(id);
   }

   @Test
   void testRetrieveDepartement_NotFound() {
      // ID fictif qui n'existe pas
      Integer id = 99;

      // Simulation d'une absence de résultat
      when(departementRepository.findById(id)).thenReturn(Optional.empty());

      // Vérifier que l'exception NoSuchElementException est levée
      assertThrows(NoSuchElementException.class, () -> {
         departementService.retrieveDepartement(id);
      });

      // Vérifier que findById() a bien été appelé
      verify(departementRepository, times(1)).findById(id);
   }
}
