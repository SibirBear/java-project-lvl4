package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.model.query.QUrl;
import hexlet.code.model.query.QUrlCheck;
import io.ebean.DB;
import io.ebean.Database;
import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class AppTest {

    private static Javalin testApp;
    private static String urlApp;
    private static Database database;
    private static MockWebServer mockWebServer;

    private static final String LOCALHOST = "http://localhost:";
    private static final int HTTP_STATUS_OK = 200;
    private static final int HTTP_STATUS_FOUND = 302;
    private static final int HTTP_STATUS_NOTFOUND = 404;
    private static final int HTTP_STATUS_UNPROCESS = 422;

    //To fill in the test database
    private static final String TEST_NAME_URL_1 = "https://hexlet.io";
    private static final String TEST_DATE_URL_1 = "2022-09-26 14:00:00";
    private static final String TEST_NAME_URL_2 = "https://cv.hexlet.io";
    private static final String TEST_DATE_URL_2 = "2022-09-26 14:10:00";
    private static final String NEW_URL_TO_ADD = "https://test.com:8080";
    private static final String PATH_TO_TEST_FILE = "src/test/resources/preform/test.html";

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
        //database.script().runScript("truncate", "TRUNCATE TABLE url RESTART IDENTITY", false);

        database.script().runScript("create", "INSERT INTO url (name, created_at) VALUES ('"
                + TEST_NAME_URL_1 + "', '" + TEST_DATE_URL_1 + "'), ('"
                + TEST_NAME_URL_2 + "', '" + TEST_DATE_URL_2 + "');",
                false);
    }

    @AfterEach
    void afterEach() {
        database.script().runScript("truncate1", "SET REFERENTIAL_INTEGRITY FALSE", false);
        database.script().runScript("truncate2", "TRUNCATE TABLE url RESTART IDENTITY", false);
        database.script().runScript("truncate3", "SET REFERENTIAL_INTEGRITY TRUE", false);
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
        void testShowUrlCorrect() {
            HttpResponse<String> response = Unirest
                    .get(urlApp + "/urls/2")
                    .asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(HTTP_STATUS_OK);
            assertThat(body).contains(TEST_NAME_URL_2);
            assertThat(body).contains("26/09/2022 14:10");

        }

        @Test
        void testShowUrlNone() {
            HttpResponse<String> response = Unirest
                    .get(urlApp + "/urls/1000")
                    .asString();

            assertThat(response.getStatus()).isEqualTo(HTTP_STATUS_NOTFOUND);

        }

        @Test
        void testNewUrlCorrect() {
            HttpResponse responsePost = Unirest
                    .post(urlApp + "/urls")
                    .field("url", NEW_URL_TO_ADD)
                    .asString();

            assertThat(responsePost.getStatus()).isEqualTo(HTTP_STATUS_FOUND);
            assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/urls");

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

        @Test
        void testNewUrlUnCorrect() {
            HttpResponse<String> responsePost = Unirest
                    .post(urlApp + "/urls")
                    .field("url", "test.com")
                    .asString();

            assertThat(responsePost.getStatus()).isEqualTo(HTTP_STATUS_UNPROCESS);
            assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("");
            assertThat(responsePost.getBody()).contains("Некорректный URL");

        }

        @Test
        void testNewUrlDublicate() {
            HttpResponse responsePost = Unirest
                    .post(urlApp + "/urls")
                    .field("url", TEST_NAME_URL_1)
                    .asString();

            assertThat(responsePost.getStatus()).isEqualTo(HTTP_STATUS_FOUND);

            HttpResponse<String> response = Unirest
                    .get(urlApp + "/urls").asString();

            assertThat(response.getStatus()).isEqualTo(HTTP_STATUS_OK);
            assertThat(response.getBody()).contains("Страница уже существует");

        }

        @Test
        void testUrlCheckModel() {
            UrlCheck urlCheck = new UrlCheck();
            Url url = new QUrl().id.equalTo(1).findOne();
            urlCheck.setUrl(url);
            urlCheck.setTitle("Title");
            urlCheck.setDescription("Description");
            urlCheck.setH1("H1");
            urlCheck.setStatusCode(200);

            assertThat(urlCheck.getTitle()).isEqualTo("Title");
            assertThat(urlCheck.getDescription()).isEqualTo("Description");
            assertThat(urlCheck.getH1()).isEqualTo("H1");
            assertThat(urlCheck.getStatusCode()).isEqualTo(200);
            assertThat(urlCheck.getUrl().getName()).isEqualTo(TEST_NAME_URL_1);

        }

        @Test
        void testUrlCheckWeb() throws IOException {
            mockWebServer = new MockWebServer();
            mockWebServer.enqueue(new MockResponse().setBody(getContentFromFile()));
            mockWebServer.start();

            String urlOriginal = mockWebServer.url("").toString();
            String urlFinal = urlOriginal.substring(0, urlOriginal.length() - 1);

            HttpResponse postNewUrl = Unirest.post(urlApp + "/urls")
                    .field("url", urlOriginal).asEmpty();

            Url url = new QUrl().name.equalTo(urlFinal).findOne();

            HttpResponse<String> postCheck = Unirest
                    .post(urlApp + "/urls/" + url.getId() + "/checks").asString();

            UrlCheck urlCheck = new QUrlCheck().url.equalTo(url).findOne();


            HttpResponse<String> resp = Unirest
                    .get(urlApp + "/urls/" + url.getId()).asString();

            String body = resp.getBody();

            assertThat(resp.getStatus()).isEqualTo(HTTP_STATUS_OK);
            assertThat(body).contains("Страница успешно проверена");
            assertThat(body).contains("Description_for_test_title");
            assertThat(body).contains("Title_test_one");
            assertThat(body).contains("Test page");

            mockWebServer.shutdown();
        }

        @Test
        void testUrlCheckException() {
            HttpResponse resp = Unirest.post(urlApp + "/urls/1111/checks").asString();
            assertThat(resp.getStatus()).isEqualTo(HTTP_STATUS_NOTFOUND);

        }

        private static String getContentFromFile() throws IOException {
            Path path = Path.of(AppTest.PATH_TO_TEST_FILE).normalize();
            return Files.readString(path);

        }

    }

}
