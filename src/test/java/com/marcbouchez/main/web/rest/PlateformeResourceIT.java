package com.marcbouchez.main.web.rest;

import com.marcbouchez.main.JeuxvideosApp;
import com.marcbouchez.main.domain.Plateforme;
import com.marcbouchez.main.repository.PlateformeRepository;
import com.marcbouchez.main.repository.search.PlateformeSearchRepository;

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
 * Integration tests for the {@link PlateformeResource} REST controller.
 */
@SpringBootTest(classes = JeuxvideosApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PlateformeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private PlateformeRepository plateformeRepository;

    /**
     * This repository is mocked in the com.marcbouchez.main.repository.search test package.
     *
     * @see com.marcbouchez.main.repository.search.PlateformeSearchRepositoryMockConfiguration
     */
    @Autowired
    private PlateformeSearchRepository mockPlateformeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlateformeMockMvc;

    private Plateforme plateforme;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plateforme createEntity(EntityManager em) {
        Plateforme plateforme = new Plateforme()
            .name(DEFAULT_NAME);
        return plateforme;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plateforme createUpdatedEntity(EntityManager em) {
        Plateforme plateforme = new Plateforme()
            .name(UPDATED_NAME);
        return plateforme;
    }

    @BeforeEach
    public void initTest() {
        plateforme = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlateforme() throws Exception {
        int databaseSizeBeforeCreate = plateformeRepository.findAll().size();
        // Create the Plateforme
        restPlateformeMockMvc.perform(post("/api/plateformes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(plateforme)))
            .andExpect(status().isCreated());

        // Validate the Plateforme in the database
        List<Plateforme> plateformeList = plateformeRepository.findAll();
        assertThat(plateformeList).hasSize(databaseSizeBeforeCreate + 1);
        Plateforme testPlateforme = plateformeList.get(plateformeList.size() - 1);
        assertThat(testPlateforme.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Plateforme in Elasticsearch
        verify(mockPlateformeSearchRepository, times(1)).save(testPlateforme);
    }

    @Test
    @Transactional
    public void createPlateformeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = plateformeRepository.findAll().size();

        // Create the Plateforme with an existing ID
        plateforme.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlateformeMockMvc.perform(post("/api/plateformes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(plateforme)))
            .andExpect(status().isBadRequest());

        // Validate the Plateforme in the database
        List<Plateforme> plateformeList = plateformeRepository.findAll();
        assertThat(plateformeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Plateforme in Elasticsearch
        verify(mockPlateformeSearchRepository, times(0)).save(plateforme);
    }


    @Test
    @Transactional
    public void getAllPlateformes() throws Exception {
        // Initialize the database
        plateformeRepository.saveAndFlush(plateforme);

        // Get all the plateformeList
        restPlateformeMockMvc.perform(get("/api/plateformes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plateforme.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getPlateforme() throws Exception {
        // Initialize the database
        plateformeRepository.saveAndFlush(plateforme);

        // Get the plateforme
        restPlateformeMockMvc.perform(get("/api/plateformes/{id}", plateforme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plateforme.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingPlateforme() throws Exception {
        // Get the plateforme
        restPlateformeMockMvc.perform(get("/api/plateformes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlateforme() throws Exception {
        // Initialize the database
        plateformeRepository.saveAndFlush(plateforme);

        int databaseSizeBeforeUpdate = plateformeRepository.findAll().size();

        // Update the plateforme
        Plateforme updatedPlateforme = plateformeRepository.findById(plateforme.getId()).get();
        // Disconnect from session so that the updates on updatedPlateforme are not directly saved in db
        em.detach(updatedPlateforme);
        updatedPlateforme
            .name(UPDATED_NAME);

        restPlateformeMockMvc.perform(put("/api/plateformes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlateforme)))
            .andExpect(status().isOk());

        // Validate the Plateforme in the database
        List<Plateforme> plateformeList = plateformeRepository.findAll();
        assertThat(plateformeList).hasSize(databaseSizeBeforeUpdate);
        Plateforme testPlateforme = plateformeList.get(plateformeList.size() - 1);
        assertThat(testPlateforme.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Plateforme in Elasticsearch
        verify(mockPlateformeSearchRepository, times(1)).save(testPlateforme);
    }

    @Test
    @Transactional
    public void updateNonExistingPlateforme() throws Exception {
        int databaseSizeBeforeUpdate = plateformeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlateformeMockMvc.perform(put("/api/plateformes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(plateforme)))
            .andExpect(status().isBadRequest());

        // Validate the Plateforme in the database
        List<Plateforme> plateformeList = plateformeRepository.findAll();
        assertThat(plateformeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Plateforme in Elasticsearch
        verify(mockPlateformeSearchRepository, times(0)).save(plateforme);
    }

    @Test
    @Transactional
    public void deletePlateforme() throws Exception {
        // Initialize the database
        plateformeRepository.saveAndFlush(plateforme);

        int databaseSizeBeforeDelete = plateformeRepository.findAll().size();

        // Delete the plateforme
        restPlateformeMockMvc.perform(delete("/api/plateformes/{id}", plateforme.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plateforme> plateformeList = plateformeRepository.findAll();
        assertThat(plateformeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Plateforme in Elasticsearch
        verify(mockPlateformeSearchRepository, times(1)).deleteById(plateforme.getId());
    }

    @Test
    @Transactional
    public void searchPlateforme() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        plateformeRepository.saveAndFlush(plateforme);
        when(mockPlateformeSearchRepository.search(queryStringQuery("id:" + plateforme.getId())))
            .thenReturn(Collections.singletonList(plateforme));

        // Search the plateforme
        restPlateformeMockMvc.perform(get("/api/_search/plateformes?query=id:" + plateforme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plateforme.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
}
