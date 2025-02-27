package tn.esprit.spring.kaddem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

@Entity
public class Universite implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUniv;

    private String nomUniv;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Departement> departements;

    // Constructeur par défaut
    public Universite() {
        // Initialisation vide ou autres actions si nécessaire
    }

    // Constructeur avec un seul paramètre
    public Universite(String nomUniv) {
        this.nomUniv = nomUniv;
    }

    // Constructeur avec id et nom
    public Universite(Integer idUniv, String nomUniv) {
        this.idUniv = idUniv;
        this.nomUniv = nomUniv;
    }

    // Constructeur avec id, nom et départements
    public Universite(Integer idUniv, String nomUniv, Set<Departement> departements) {
        this.idUniv = idUniv;
        this.nomUniv = nomUniv;
        this.departements = departements;
    }

    // Getters et Setters
    public Set<Departement> getDepartements() {
        return departements;
    }

    public void setDepartements(Set<Departement> departements) {
        this.departements = departements;
    }

    public Integer getIdUniv() {
        return idUniv;
    }

    public void setIdUniv(Integer idUniv) {
        this.idUniv = idUniv;
    }

    public String getNomUniv() {
        return nomUniv;
    }

    public void setNomUniv(String nomUniv) {
        this.nomUniv = nomUniv;
    }
}
