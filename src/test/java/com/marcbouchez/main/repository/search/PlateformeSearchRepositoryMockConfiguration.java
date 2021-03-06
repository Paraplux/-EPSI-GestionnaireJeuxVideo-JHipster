package com.marcbouchez.main.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PlateformeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PlateformeSearchRepositoryMockConfiguration {

    @MockBean
    private PlateformeSearchRepository mockPlateformeSearchRepository;

}
