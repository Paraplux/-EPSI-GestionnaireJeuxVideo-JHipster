package com.marcbouchez.main.web.rest;

import com.marcbouchez.main.JeuxvideosApp;
import com.marcbouchez.main.domain.Jeu;
import com.marcbouchez.main.repository.JeuRepository;
import com.marcbouchez.main.repository.search.JeuSearchRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link JeuResource} REST controller.
 */
@SpringBootTest(classes = JeuxvideosApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class JeuResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_PRIX = 1F;
    private static final Float UPDATED_PRIX = 2F;

    @Autowired
    private JeuRepository jeuRepository;

    /**
     * This repository is mocked in the com.marcbouchez.main.repository.search test package.
     *
     * @see com.marcbouchez.main.repository.search.JeuSearchRepositoryMockConfiguration
     */
    @Autowired
    private JeuSearchRepository mockJeuSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJeuMockMvc;

    private Jeu jeu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Jeu createEntity(EntityManager em) {
        Jeu jeu = new Jeu()
            .name(DEFAULT_NAME)
            .prix(DEFAULT_PRIX);
        return jeu;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Jeu createUpdatedEntity(EntityManager em) {
        Jeu jeu = new Jeu()
            .name(UPDATED_NAME)
            .prix(UPDATED_PRIX);
        return jeu;
    }

    @BeforeEach
    public void initTest() {
        jeu = createEntity(em);
    }

    @Test
    @Transactional
    public void createJeu() throws Exception {
        int databaseSizeBeforeCreate = jeuRepository.findAll().size();
        // Create the Jeu
        restJeuMockMvc.perform(post("/api/jeus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jeu)))
            .andExpect(status().isCreated());

        // Validate the Jeu in the database
        List<Jeu> jeuList = jeuRepository.findAll();
        assertThat(jeuList).hasSize(databaseSizeBeforeCreate + 1);
        Jeu testJeu = jeuList.get(jeuList.size() - 1);
        assertThat(testJeu.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testJeu.getPrix()).isEqualTo(DEFAULT_PRIX);

        // Validate the Jeu in Elasticsearch
        verify(mockJeuSearchRepository, times(1)).save(testJeu);
    }

    @Test
    @Transactional
    public void createJeuWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jeuRepository.findAll().size();

        // Create the Jeu with an existing ID
        jeu.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJeuMockMvc.perform(post("/api/jeus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jeu)))
            .andExpect(status().isBadRequest());

        // Validate the Jeu in the database
        List<Jeu> jeuList = jeuRepository.findAll();
        assertThat(jeuList).hasSize(databaseSizeBeforeCreate);

        // Validate the Jeu in Elasticsearch
        verify(mockJeuSearchRepository, times(0)).save(jeu);
    }


    @Test
    @Transactional
    public void getAllJeus() throws Exception {
        // Initialize the database
        jeuRepository.saveAndFlush(jeu);

        // Get all the jeuList
        restJeuMockMvc.perform(get("/api/jeus?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jeu.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getJeu() throws Exception {
        // Initialize the database
        jeuRepository.saveAndFlush(jeu);

        // Get the jeu
        restJeuMockMvc.perform(get("/api/jeus/{id}", jeu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jeu.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.prix").value(DEFAULT_PRIX.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingJeu() throws Exception {
        // Get the jeu
        restJeuMockMvc.perform(get("/api/jeus/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJeu() throws Exception {
        // Initialize the database
        jeuRepository.saveAndFlush(jeu);

        int databaseSizeBeforeUpdate = jeuRepository.findAll().size();

        // Update the jeu
        Jeu updatedJeu = jeuRepository.findById(jeu.getId()).get();
        // Disconnect from session so that the updates on updatedJeu are not directly saved in db
        em.detach(updatedJeu);
        updatedJeu
            .name(UPDATED_NAME)
            .prix(UPDATED_PRIX);

        restJeuMockMvc.perform(put("/api/jeus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedJeu)))
            .andExpect(status().isOk());

        // Validate the Jeu in the database
        List<Jeu> jeuList = jeuRepository.findAll();
        assertThat(jeuList).hasSize(databaseSizeBeforeUpdate);
        Jeu testJeu = jeuList.get(jeuList.size() - 1);
        assertThat(testJeu.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testJeu.getPrix()).isEqualTo(UPDATED_PRIX);

        // Validate the Jeu in Elasticsearch
        verify(mockJeuSearchRepository, times(1)).save(testJeu);
    }

    @Test
    @Transactional
    public void updateNonExistingJeu() throws Exception {
        int databaseSizeBeforeUpdate = jeuRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJeuMockMvc.perform(put("/api/jeus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jeu)))
            .andExpect(status().isBadRequest());

        // Validate the Jeu in the database
        List<Jeu> jeuList = jeuRepository.findAll();
        assertThat(jeuList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Jeu in Elasticsearch
        verify(mockJeuSearchRepository, times(0)).save(jeu);
    }

    @Test
    @Transactional
    public void deleteJeu() throws Exception {
        // Initialize the database
        jeuRepository.saveAndFlush(jeu);

        int databaseSizeBeforeDelete = jeuRepository.findAll().size();

        // Delete the jeu
        restJeuMockMvc.perform(delete("/api/jeus/{id}", jeu.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Jeu> jeuList = jeuRepository.findAll();
        assertThat(jeuList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Jeu in Elasticsearch
        verify(mockJeuSearchRepository, times(1)).deleteById(jeu.getId());
    }

    @Test
    @Transactional
    public void searchJeu() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        jeuRepository.saveAndFlush(jeu);
        when(mockJeuSearchRepository.search(queryStringQuery("id:" + jeu.getId())))
            .thenReturn(Collections.singletonList(jeu));

        // Search the jeu
        restJeuMockMvc.perform(get("/api/_search/jeus?query=id:" + jeu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jeu.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.doubleValue())));
    }
}
