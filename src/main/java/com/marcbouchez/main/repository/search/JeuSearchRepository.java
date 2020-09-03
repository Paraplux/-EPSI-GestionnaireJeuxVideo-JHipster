package com.marcbouchez.main.repository.search;

import com.marcbouchez.main.domain.Jeu;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Jeu} entity.
 */
public interface JeuSearchRepository extends ElasticsearchRepository<Jeu, Long> {
}
