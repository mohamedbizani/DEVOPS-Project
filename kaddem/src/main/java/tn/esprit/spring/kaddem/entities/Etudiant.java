package tn.esprit.spring.kaddem.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;


@SuppressWarnings("SpellCheckingInspection")
@Entity
public class Etudiant implements Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer idEtudiant;
    private String nomE;
    private String prenomE;
    @Enumerated(EnumType.STRING)
    private Option op;
    @OneToMany(mappedBy="etudiant", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Contrat> contrats;
    @ManyToOne
    @JsonIgnore
    private Departement departement;
  //  @ManyToMany(cascade =CascadeType.ALL)
    @ManyToMany(mappedBy="etudiants")

    @JsonIgnore

    private Set<Equipe> equipes ;
    public Etudiant() {
    }


    public Etudiant(String nomE, String prenomE) {
        this.nomE = nomE;
        this.prenomE = prenomE;
    }

    public Etudiant(String nomE, String prenomE, Option op) {
        super();
        this.nomE = nomE;
        this.prenomE = prenomE;
        this.op = op;
    }

    public Etudiant(Integer idEtudiant, String nomE, String prenomE, Option op) {
        super();
        this.idEtudiant = idEtudiant;
        this.nomE = nomE;
        this.prenomE = prenomE;
        this.op = op;
    }

    public Set<Contrat> getContrats() {
        return contrats;
    }

    public void setContrats(Set<Contrat> contrats) {
        this.contrats = contrats;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Set<Equipe> getEquipes() {
        return equipes;
    }

    public void setEquipes(Set<Equipe> equipes) {
        this.equipes = equipes;
    }

    public Integer getIdEtudiant() {
        return idEtudiant;
    }
    public void setIdEtudiant(Integer idEtudiant) {
        this.idEtudiant = idEtudiant;
    }
    public String getNomE() {
        return nomE;
    }
    public void setNomE(String nomE) {
        this.nomE = nomE;
    }
    public String getPrenomE() {
        return prenomE;
    }
    public void setPrenomE(String prenomE) {
        this.prenomE = prenomE;
    }
    public Option getOp() {
        return op;
    }
    public void setOp(Option op) {
        this.op = op;
    }

}
