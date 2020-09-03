package com.marcbouchez.main.web.rest;

import com.marcbouchez.main.domain.Jeu;
import com.marcbouchez.main.repository.JeuRepository;
import com.marcbouchez.main.repository.search.JeuSearchRepository;
import com.marcbouchez.main.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.marcbouchez.main.domain.Jeu}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class JeuResource {

    private final Logger log = LoggerFactory.getLogger(JeuResource.class);

    private static final String ENTITY_NAME = "jeu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JeuRepository jeuRepository;

    private final JeuSearchRepository jeuSearchRepository;

    public JeuResource(JeuRepository jeuRepository, JeuSearchRepository jeuSearchRepository) {
        this.jeuRepository = jeuRepository;
        this.jeuSearchRepository = jeuSearchRepository;
    }

    /**
     * {@code POST  /jeus} : Create a new jeu.
     *
     * @param jeu the jeu to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jeu, or with status {@code 400 (Bad Request)} if the jeu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/jeus")
    public ResponseEntity<Jeu> createJeu(@RequestBody Jeu jeu) throws URISyntaxException {
        log.debug("REST request to save Jeu : {}", jeu);
        if (jeu.getId() != null) {
            throw new BadRequestAlertException("A new jeu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Jeu result = jeuRepository.save(jeu);
        jeuSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/jeus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /jeus} : Updates an existing jeu.
     *
     * @param jeu the jeu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jeu,
     * or with status {@code 400 (Bad Request)} if the jeu is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jeu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/jeus")
    public ResponseEntity<Jeu> updateJeu(@RequestBody Jeu jeu) throws URISyntaxException {
        log.debug("REST request to update Jeu : {}", jeu);
        if (jeu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Jeu result = jeuRepository.save(jeu);
        jeuSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jeu.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /jeus} : get all the jeus.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jeus in body.
     */
    @GetMapping("/jeus")
    public List<Jeu> getAllJeus() {
        log.debug("REST request to get all Jeus");
        return jeuRepository.findAll();
    }

    /**
     * {@code GET  /jeus/:id} : get the "id" jeu.
     *
     * @param id the id of the jeu to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jeu, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/jeus/{id}")
    public ResponseEntity<Jeu> getJeu(@PathVariable Long id) {
        log.debug("REST request to get Jeu : {}", id);
        Optional<Jeu> jeu = jeuRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(jeu);
    }

    /**
     * {@code DELETE  /jeus/:id} : delete the "id" jeu.
     *
     * @param id the id of the jeu to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/jeus/{id}")
    public ResponseEntity<Void> deleteJeu(@PathVariable Long id) {
        log.debug("REST request to delete Jeu : {}", id);
        jeuRepository.deleteById(id);
        jeuSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/jeus?query=:query} : search for the jeu corresponding
     * to the query.
     *
     * @param query the query of the jeu search.
     * @return the result of the search.
     */
    @GetMapping("/_search/jeus")
    public List<Jeu> searchJeus(@RequestParam String query) {
        log.debug("REST request to search Jeus for query {}", query);
        return StreamSupport
            .stream(jeuSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
