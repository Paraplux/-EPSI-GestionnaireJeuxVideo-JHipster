package com.marcbouchez.main.repository.search;

import com.marcbouchez.main.domain.Plateforme;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Plateforme} entity.
 */
public interface PlateformeSearchRepository extends ElasticsearchRepository<Plateforme, Long> {
}
