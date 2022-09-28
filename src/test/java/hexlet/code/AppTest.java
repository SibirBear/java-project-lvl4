package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.model.query.QUrl;
import io.ebean.DB;
import io.ebean.Database;
import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class AppTest {

    private static Javalin testApp;
    private static String urlApp;
    private static Database database;

    private static final String LOCALHOST = "http://localhost:";
    private static final int HTTP_STATUS_OK = 200;
    private static final int HTTP_STATUS_FOUND = 302;

    //To fill in the test database
    private static final String TEST_NAME_URL_1 = "https://hexlet.io";
    private static final String TEST_DATE_URL_1 = "2022-09-26 14:00:00";
    private static final String TEST_NAME_URL_2 = "https://cv.hexlet.io";
    private static final String TEST_DATE_URL_2 = "2022-09-26 14:10:00";
    private static final String NEW_URL_TO_ADD = "https://test.com";

    @BeforeAll
    public static void beforeAll() {
        testApp = App.getApp();
        testApp.start();
        int port = testApp.port();
        urlApp = LOCALHOST + port;
        database = DB.getDefault();
    }

    @AfterAll
    public static void afterAll() {
        testApp.stop();
    }

    @BeforeEach
    void beforeEach() {
        database.script().runScript("truncate", "TRUNCATE TABLE url RESTART IDENTITY", false);
        database.script().runScript("create", "INSERT INTO url (name, created_at) VALUES ('"
                + TEST_NAME_URL_1 + "', '" + TEST_DATE_URL_1 + "'), ('"
                + TEST_NAME_URL_2 + "', '" + TEST_DATE_URL_2 + "');",
                false);
    }

    @Test
    void testRoot() {
        HttpResponse<String> response = Unirest.get(urlApp).asString();
        assertThat(response.getStatus()).isEqualTo(HTTP_STATUS_OK);
        assertThat(response.getBody()).contains("Анализатор страниц");
    }

    @Nested
    class UrlTest {
        @Test
        void testListWeb() {
            HttpResponse<String> response = Unirest
                    .get(urlApp + "/urls")
                    .asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(HTTP_STATUS_OK);
            assertThat(body).contains(TEST_NAME_URL_1);
            assertThat(body).contains(TEST_NAME_URL_2);

        }

        @Test
        void testListDB() {
            List<Url> urlsList = new QUrl().findList();

            assertThat(urlsList.size()).isEqualTo(2);

        }

        @Test
        void testShowUrl() {
            HttpResponse<String> response = Unirest
                    .get(urlApp + "/urls/2")
                    .asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(HTTP_STATUS_OK);
            assertThat(body).contains(TEST_NAME_URL_2);
            assertThat(body).contains("26/09/2022 14:10");

        }

        @Test
        void testNewUrl() {

            HttpResponse responsePost = Unirest
                    .post(urlApp + "/urls")
                    .field("url", NEW_URL_TO_ADD)
                    .asString();

            assertThat(responsePost.getStatus()).isEqualTo(HTTP_STATUS_FOUND);
            assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("urls");

            HttpResponse<String> response = Unirest
                    .get(urlApp + "/urls").asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(HTTP_STATUS_OK);
            assertThat(body).contains(NEW_URL_TO_ADD);
            assertThat(body).contains("Страница успешно добавлена");

            Url actualUrl = new QUrl()
                    .name.equalTo(NEW_URL_TO_ADD).findOne();

            assertThat(actualUrl).isNotNull();
            assertThat(actualUrl.getName()).isEqualTo(NEW_URL_TO_ADD);

        }
    }

}
