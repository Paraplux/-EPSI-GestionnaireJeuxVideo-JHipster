package com.marcbouchez.main.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.marcbouchez.main.web.rest.TestUtil;

public class PlateformeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Plateforme.class);
        Plateforme plateforme1 = new Plateforme();
        plateforme1.setId(1L);
        Plateforme plateforme2 = new Plateforme();
        plateforme2.setId(plateforme1.getId());
        assertThat(plateforme1).isEqualTo(plateforme2);
        plateforme2.setId(2L);
        assertThat(plateforme1).isNotEqualTo(plateforme2);
        plateforme1.setId(null);
        assertThat(plateforme1).isNotEqualTo(plateforme2);
    }
}
