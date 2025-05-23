package org.tkit.onecx.announcement.rs.internal;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.tkit.onecx.announcement.domain.daos.AnnouncementDAO;
import org.tkit.onecx.announcement.rs.internal.controller.AnnouncementControllerInternal;
import org.tkit.onecx.announcement.test.AbstractTest;
import org.tkit.quarkus.jpa.exceptions.DAOException;
import org.tkit.quarkus.security.test.GenerateKeycloakClient;

import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestHTTPEndpoint(AnnouncementControllerInternal.class)
@GenerateKeycloakClient(clientName = "testClient", scopes = { "ocx-an:read", "ocx-an:write", "ocx-an:delete", "ocx-an:all" })
class AnnouncementControllerInternalExceptionTest extends AbstractTest {

    @InjectMock
    AnnouncementDAO dao;

    @BeforeEach
    void beforeAll() {
        Mockito.when(dao.findProductsWithAnnouncements())
                .thenThrow(new RuntimeException("Test technical error exception"))
                .thenThrow(new DAOException(AnnouncementDAO.ErrorKeys.ERROR_FIND_PRODUCTS_WITH_ANNOUNCEMENTS,
                        new RuntimeException("Test")));
    }

    @Test
    void exceptionTest() {
        given()
                .auth().oauth2(getKeycloakClientToken("testClient"))
                .contentType(APPLICATION_JSON)
                .get("products")
                .then()
                .statusCode(INTERNAL_SERVER_ERROR.getStatusCode());

        given()
                .auth().oauth2(getKeycloakClientToken("testClient"))
                .contentType(APPLICATION_JSON)
                .get("products")
                .then()
                .statusCode(INTERNAL_SERVER_ERROR.getStatusCode());

    }
}
