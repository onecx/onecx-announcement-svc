package org.tkit.onecx.announcement.rs.internal.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;
import org.tkit.onecx.announcement.test.AbstractTest;

import gen.org.tkit.onecx.announcement.rs.internal.model.AnnouncementProductsDTO;
import gen.org.tkit.onecx.announcement.rs.internal.model.ProblemDetailParamDTO;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class AnnouncementMapperTest extends AbstractTest {

    @Inject
    AnnouncementMapper mapper;

    @Test
    void mapProductsAndWorkspacesTest() {
        assertThat(mapper.map((List<String>) null, (List<String>) null)).isNull();

        AnnouncementProductsDTO productOnly = mapper.map(List.of("product1"), null);
        assertThat(productOnly).isNotNull();
        assertThat(productOnly.getProductNames()).containsExactly("product1");
        assertThat(productOnly.getWorkspaceNames()).isEmpty();

        AnnouncementProductsDTO workspaceOnly = mapper.map(null, List.of("workspace1"));
        assertThat(workspaceOnly).isNotNull();
        assertThat(workspaceOnly.getProductNames()).isEmpty();
        assertThat(workspaceOnly.getWorkspaceNames()).containsExactly("workspace1");

        AnnouncementProductsDTO both = mapper.map(List.of("product1"), List.of("workspace1"));
        assertThat(both).isNotNull();
        assertThat(both.getProductNames()).containsExactly("product1");
        assertThat(both.getWorkspaceNames()).containsExactly("workspace1");
    }

    @Test
    void mapParamsTest() {
        assertThat(mapper.map((Map<String, Object>) null)).isEmpty();

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("key1", "value1");
        params.put("key2", null);

        List<ProblemDetailParamDTO> result = mapper.map(params);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(ProblemDetailParamDTO::getKey).containsExactly("key1", "key2");
        assertThat(result.get(0).getValue()).isEqualTo("value1");
        assertThat(result.get(1).getValue()).isNull();
    }
}
