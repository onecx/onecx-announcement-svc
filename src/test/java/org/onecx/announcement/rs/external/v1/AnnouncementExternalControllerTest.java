package org.onecx.announcement.rs.external.v1;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;
import org.tkit.quarkus.test.WithDBData;

import gen.io.github.onecx.announcement.v1.model.*;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class AnnouncementExternalControllerTest {
    String ANNOUNCEMENT_CONTROLLER_V1_ENDPOINT = "/v1/applications";

    @Test
    @WithDBData(value = { "ahm-testdata.xml" }, deleteAfterTest = true, deleteBeforeInsert = true)
    void shouldSuccessfullySearchForOneResultWithId() throws ParseException {
        String fetchedId = "123";
        String creationUser = "Test User";
        var response = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .get(ANNOUNCEMENT_CONTROLLER_V1_ENDPOINT + "/announcements/" + fetchedId)
                .prettyPeek();

        AnnouncementDTOV1 announcements = response.as(AnnouncementDTOV1.class);
        // then
        response.then().statusCode(200);
        assertThat(announcements.getId()).isEqualTo(fetchedId);
        assertThat(announcements.getCreationUser()).isEqualTo(creationUser);
    }

    @Test
    @WithDBData(value = { "ahm-testdata.xml" }, deleteAfterTest = true, deleteBeforeInsert = true)
    void shouldSuccessfullySearchForOneResultWithCriteriaV1() throws ParseException {
        String announcementId = "123";
        String creationUser = "Test User";
        OffsetDateTime startDateFrom = OffsetDateTime.parse("2009-12-30T14:55:00+00:00");
        OffsetDateTime startDateTo = OffsetDateTime.parse("2010-01-01T14:55:00+00:00");

        SearchAnnouncementRequestDTOV1 searchAnnouncementRequestDTOV1 = new SearchAnnouncementRequestDTOV1();
        searchAnnouncementRequestDTOV1.setStartDateFrom(startDateFrom);
        searchAnnouncementRequestDTOV1.setStartDateTo(startDateTo);

        searchAnnouncementRequestDTOV1.setType(AnnouncementTypeDTOV1.EVENT);
        var response = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(searchAnnouncementRequestDTOV1)
                .post(ANNOUNCEMENT_CONTROLLER_V1_ENDPOINT + "/announcements/search/")
                .prettyPeek();

        AnnouncementPageResultDTOV1 announcements = response.as(AnnouncementPageResultDTOV1.class);
        // then
        response.then().statusCode(200);
        assertThat(announcements.getTotalElements()).isEqualTo(1);
        assertThat(announcements.getStream().get(0).getId()).isEqualTo(announcementId);
        assertThat(announcements.getStream().get(0).getCreationUser()).isEqualTo(creationUser);
    }

    @Test
    @WithDBData(value = "ahm-testdata.xml", deleteAfterTest = true, deleteBeforeInsert = true)
    void shouldSuccessfullyFetchAllAnnouncementsV1() throws ParseException {
        SearchAnnouncementRequestDTOV1 searchAnnouncementRequestDTOV1 = new SearchAnnouncementRequestDTOV1();
        var response = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(searchAnnouncementRequestDTOV1)
                .post(ANNOUNCEMENT_CONTROLLER_V1_ENDPOINT + "/announcements/search")
                .prettyPeek();

        AnnouncementPageResultDTOV1 announcements = response.as(AnnouncementPageResultDTOV1.class);
        // then
        response.then().statusCode(200);
        assertThat(announcements.getTotalElements()).isEqualTo(500);
    }

    @Test
    @WithDBData(value = "ahm-testdata.xml", deleteAfterTest = true, deleteBeforeInsert = true)
    void shouldSuccessfullyFetchAnnouncementsWithPageSize10() throws ParseException {
        int pageSize = 10;
        int pageNumber = 7;
        SearchAnnouncementRequestDTOV1 searchAnnouncementRequestDTOV1 = new SearchAnnouncementRequestDTOV1();
        searchAnnouncementRequestDTOV1.setPageSize(pageSize);
        searchAnnouncementRequestDTOV1.setPageNumber(pageNumber);
        var response = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(searchAnnouncementRequestDTOV1)
                .post(ANNOUNCEMENT_CONTROLLER_V1_ENDPOINT + "/announcements/search")
                .prettyPeek();

        AnnouncementPageResultDTOV1 announcements = response.as(AnnouncementPageResultDTOV1.class);
        // then
        response.then().statusCode(200);
        assertThat(announcements.getTotalElements()).isEqualTo(500);
        assertThat(announcements.getTotalPages()).isEqualTo(50);
        assertThat(announcements.getSize()).isEqualTo(pageSize);
        assertThat(announcements.getNumber()).isEqualTo(pageNumber);
    }

    @Test
    @WithDBData(value = "ahm-testdata.xml", deleteAfterTest = true, deleteBeforeInsert = true)
    void shouldSuccessfullyFetchAnnouncementsWithPageSize100() throws ParseException {
        int pageSize = 100;
        int pageNumber = 0;
        SearchAnnouncementRequestDTOV1 searchAnnouncementRequestDTOV1 = new SearchAnnouncementRequestDTOV1();
        searchAnnouncementRequestDTOV1.setPageSize(pageSize);
        searchAnnouncementRequestDTOV1.setPageNumber(pageNumber);
        var response = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(searchAnnouncementRequestDTOV1)
                .post(ANNOUNCEMENT_CONTROLLER_V1_ENDPOINT + "/announcements/search")
                .prettyPeek();

        AnnouncementPageResultDTOV1 announcements = response.as(AnnouncementPageResultDTOV1.class);
        // then
        response.then().statusCode(200);
        assertThat(announcements.getTotalElements()).isEqualTo(500);
        assertThat(announcements.getTotalPages()).isEqualTo(5);
        assertThat(announcements.getSize()).isEqualTo(pageSize);
        assertThat(announcements.getNumber()).isEqualTo(pageNumber);
    }

    @Test
    @WithDBData(value = { "ahm-testdata.xml" }, deleteAfterTest = true, deleteBeforeInsert = true)
    void shouldSuccessfullyFetchAnnouncementsWithPageSize200() throws ParseException {
        int pageSize = 200;
        int pageNumber = 1;
        SearchAnnouncementRequestDTOV1 searchAnnouncementRequestDTOV1 = new SearchAnnouncementRequestDTOV1();
        searchAnnouncementRequestDTOV1.setPageSize(pageSize);
        searchAnnouncementRequestDTOV1.setPageNumber(pageNumber);
        var response = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(searchAnnouncementRequestDTOV1)
                .post(ANNOUNCEMENT_CONTROLLER_V1_ENDPOINT + "/announcements/search")
                .prettyPeek();

        AnnouncementPageResultDTOV1 announcements = response.as(AnnouncementPageResultDTOV1.class);
        // then
        response.then().statusCode(200);
        assertThat(announcements.getTotalElements()).isEqualTo(500);
        assertThat(announcements.getTotalPages()).isEqualTo(3);
        assertThat(announcements.getSize()).isEqualTo(pageSize);
        assertThat(announcements.getNumber()).isEqualTo(pageNumber);
    }

    @Test
    @WithDBData(value = { "ahm-testdata.xml" }, deleteAfterTest = true, deleteBeforeInsert = true)
    void shouldSuccessfullyFetchAnnouncementsWithPageSize500() throws ParseException {
        int pageSize = 500;
        int pageNumber = 0;
        SearchAnnouncementRequestDTOV1 searchAnnouncementRequestDTOV1 = new SearchAnnouncementRequestDTOV1();
        searchAnnouncementRequestDTOV1.setPageSize(pageSize);
        searchAnnouncementRequestDTOV1.setPageNumber(pageNumber);
        var response = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(searchAnnouncementRequestDTOV1)
                .post(ANNOUNCEMENT_CONTROLLER_V1_ENDPOINT + "/announcements/search")
                .prettyPeek();

        AnnouncementPageResultDTOV1 announcements = response.as(AnnouncementPageResultDTOV1.class);
        // then
        response.then().statusCode(200);
        assertThat(announcements.getTotalElements()).isEqualTo(500);
        assertThat(announcements.getTotalPages()).isEqualTo(1);
        assertThat(announcements.getSize()).isEqualTo(pageSize);
        assertThat(announcements.getNumber()).isEqualTo(pageNumber);
    }

    @Test
    @WithDBData(value = { "ahm-testdata.xml" }, deleteAfterTest = true, deleteBeforeInsert = true)
    void shouldSuccessfullyDeleteAnnouncementsWithAppIds() throws ParseException {
        List<String> appIds = new ArrayList<>();
        appIds.add("support-tool-ui");
        DeleteAnnouncementRequestDTOV1 deleteAnnouncementRequestDTOV1 = new DeleteAnnouncementRequestDTOV1();
        deleteAnnouncementRequestDTOV1.setAppIds(appIds);

        var response = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(deleteAnnouncementRequestDTOV1)
                .delete(ANNOUNCEMENT_CONTROLLER_V1_ENDPOINT)
                .prettyPeek();

        response.then().statusCode(204);
    }
}
