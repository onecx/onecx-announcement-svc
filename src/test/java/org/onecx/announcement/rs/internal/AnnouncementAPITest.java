package org.onecx.announcement.rs.internal;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.time.*;

import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;
import org.tkit.quarkus.test.WithDBData;

import gen.io.github.onecx.announcement.rs.internal.model.*;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class AnnouncementAPITest {
    String ANNOUNCEMENT_ITEM_INTERNAL_ENDPOINT = "/internal/announcements";

    @Test
    @WithDBData(value = { "ahm-testdata.xml" }, deleteAfterTest = true, deleteBeforeInsert = true)
    public void shouldSuccessfullySearchForOneResultWithCriteria() throws ParseException {
        String fetchedId = "123";
        SearchAnnouncementRequestDTO searchAnnouncementRequestDTO = new SearchAnnouncementRequestDTO();

        //        String enddate = "2010-01-01T14:55:00+00:00";
        //        String startdate = "2009-12-30T14:55:00+00:00";
        //2023-12-05T01:35:30.20912803+01:00
        //        searchAnnouncementRequestDTO.setStartDateTo(OffsetDateTime.parse(startdate, DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        //        searchAnnouncementRequestDTO.setStartDateFrom(OffsetDateTime.parse(enddate, DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        searchAnnouncementRequestDTO.setType(AnnouncementTypeDTO.EVENT);
        searchAnnouncementRequestDTO.setId(fetchedId);

        //         when
        var response = given()
                .when()
                .body(searchAnnouncementRequestDTO)
                .contentType(MediaType.APPLICATION_JSON)
                .post(ANNOUNCEMENT_ITEM_INTERNAL_ENDPOINT + "/search")
                .prettyPeek();

        AnnouncementPageResultDTO announcementPageResultDTO = response.as(AnnouncementPageResultDTO.class);
        // then
        response.then().statusCode(200);
        assertThat(announcementPageResultDTO.getTotalElements()).isEqualTo(1);
        assertThat(announcementPageResultDTO.getStream().get(0).getId()).isEqualTo(fetchedId);
    }

    @Test
    @WithDBData(value = { "ahm-testdata.xml" }, deleteAfterTest = true, deleteBeforeInsert = true)
    public void shouldSuccessfullyFetchAllAnnouncements() throws ParseException {
        //         when
        SearchAnnouncementRequestDTO searchAnnouncementRequestDTO = new SearchAnnouncementRequestDTO();
        var response = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(searchAnnouncementRequestDTO)
                .post(ANNOUNCEMENT_ITEM_INTERNAL_ENDPOINT + "/search")
                .prettyPeek();

        AnnouncementPageResultDTO announcementPageResultDTO = response.as(AnnouncementPageResultDTO.class);
        // then
        response.then().statusCode(200);
        assertThat(announcementPageResultDTO.getTotalElements()).isEqualTo(500);
    }

    @Test
    @WithDBData(value = { "ahm-testdata.xml" }, deleteAfterTest = true, deleteBeforeInsert = true)
    public void shouldSuccessfullyDeleteAnnouncement() {
        String announcementId = "123";
        //         when
        var deleteResponse = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .delete(ANNOUNCEMENT_ITEM_INTERNAL_ENDPOINT + '/' + announcementId)
                .prettyPeek();

        // then
        deleteResponse.then().statusCode(204);

        var getResponse = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .get(ANNOUNCEMENT_ITEM_INTERNAL_ENDPOINT + '/' + announcementId)
                .prettyPeek();

        // then
        getResponse.then().statusCode(404);
    }

    @Test
    @WithDBData(value = { "ahm-testdata.xml" }, deleteAfterTest = true, deleteBeforeInsert = true)
    public void shouldSuccessfullyFetchAnnouncementWithId() throws ParseException {
        String announcementId = "123";
        //         when
        var response = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .get(ANNOUNCEMENT_ITEM_INTERNAL_ENDPOINT + "/" + announcementId)
                .prettyPeek();

        AnnouncementDTO announcementDTO = response.as(AnnouncementDTO.class);
        // then
        response.then().statusCode(200);
        assertThat(announcementDTO.getId()).isEqualTo(announcementId);
    }

    @Test
    @WithDBData(value = { "ahm-testdata.xml" }, deleteAfterTest = true, deleteBeforeInsert = true)
    public void shouldCorrectlyCreateAnnouncement() {
        CreateAnnouncementRequestDTO announcementCreateDTO = new CreateAnnouncementRequestDTO();
        String appId = "support-tool-ui";
        String content = "Some content of the announcement";
        String title = "Important Announcement";
        AnnouncementTypeDTO type = AnnouncementTypeDTO.INFO;

        announcementCreateDTO.setAppId(appId);
        announcementCreateDTO.setContent(content);
        announcementCreateDTO.setTitle(title);
        announcementCreateDTO.setType(type);
        //         when
        var createResponse = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(announcementCreateDTO)
                .post(ANNOUNCEMENT_ITEM_INTERNAL_ENDPOINT)
                .prettyPeek();

        // then
        createResponse.then().statusCode(201);
        AnnouncementDTO announcementDTO = createResponse.as(AnnouncementDTO.class);
        assertThat(announcementDTO.getAppId()).isEqualTo(appId);
        assertThat(announcementDTO.getType()).isEqualTo(type);
        assertThat(announcementDTO.getTitle()).isEqualTo(title);
    }

    @Test
    @WithDBData(value = { "ahm-testdata.xml" }, deleteAfterTest = true, deleteBeforeInsert = true)
    public void shouldSuccessfullyUpdateAnnouncement() throws ParseException {
        String announcementId = "123";
        UpdateAnnouncementRequestDTO updateAnnouncementRequestDTO = new UpdateAnnouncementRequestDTO();
        String appId = "ahm-ui";

        updateAnnouncementRequestDTO.setAppId(appId);
        updateAnnouncementRequestDTO.setPriority(AnnouncementPriorityTypeDTO.LOW);
        updateAnnouncementRequestDTO.setType(AnnouncementTypeDTO.INFO);
        //         when
        var patchResponse = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateAnnouncementRequestDTO)
                .put(ANNOUNCEMENT_ITEM_INTERNAL_ENDPOINT + '/' + announcementId)
                .prettyPeek();

        // then
        patchResponse.then().statusCode(200);

        AnnouncementDTO response = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .get(ANNOUNCEMENT_ITEM_INTERNAL_ENDPOINT + '/' + announcementId)
                .then()
                .statusCode(200)
                .extract().body().as(AnnouncementDTO.class);

        // then
        assertThat(response.getAppId()).isEqualTo(appId);
        assertThat(response.getPriority()).isEqualTo(AnnouncementPriorityTypeDTO.LOW);
        assertThat(response.getType()).isEqualTo(AnnouncementTypeDTO.INFO);
    }
}
