package com.marcbouchez.main.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Jeu.
 */
@Entity
@Table(name = "jeu")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "jeu")
public class Jeu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "prix")
    private Float prix;

    @ManyToOne
    @JsonIgnoreProperties(value = "jeus", allowSetters = true)
    private Plateforme plateforme;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Jeu name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrix() {
        return prix;
    }

    public Jeu prix(Float prix) {
        this.prix = prix;
        return this;
    }

    public void setPrix(Float prix) {
        this.prix = prix;
    }

    public Plateforme getPlateforme() {
        return plateforme;
    }

    public Jeu plateforme(Plateforme plateforme) {
        this.plateforme = plateforme;
        return this;
    }

    public void setPlateforme(Plateforme plateforme) {
        this.plateforme = plateforme;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Jeu)) {
            return false;
        }
        return id != null && id.equals(((Jeu) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Jeu{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", prix=" + getPrix() +
            "}";
    }
}
