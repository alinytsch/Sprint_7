package courier;

import base.BaseTest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.*;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CourierLoginTest extends BaseTest {

    private String login;
    private final String password = "1234";
    private final String firstName = "Naruto";
    private int courierId;

    @Step("Генерация уникального логина")
    public String generateLogin() {
        return "courier" + new Random().nextInt(999999);
    }

    @Step("Создание курьера")
    public void createCourier() {
        given()
                .header("Content-type", "application/json")
                .body("{\"login\":\"" + login + "\", \"password\":\"" + password + "\", \"firstName\":\"" + firstName + "\"}")
                .post("/api/v1/courier")
                .then()
                .statusCode(201);
    }

    @Step("Попытка логина курьера")
    public Response loginCourier(String login, String password) {
        return given()
                .header("Content-type", "application/json")
                .body("{\"login\":\"" + login + "\", \"password\":\"" + password + "\"}")
                .post("/api/v1/courier");
    }

    @Step("Логин курьера и получение ID")
    public int getCourierId() {
        return given()
                .header("Content-type", "application/json")
                .body("{\"login\":\"" + login + "\", \"password\":\"" + password + "\"}")
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    @Step("Удаление курьера")
    public void deleteCourier(int id) {
        given()
                .delete("/api/v1/courier/" + id)
                .then()
                .statusCode(200);
    }

    @Before
    public void setUp() {
        login = generateLogin();
        createCourier();
        courierId = getCourierId();
    }

    @Test
    public void courierCanLogin() {
        given()
                .header("Content-type", "application/json")
                .body("{\"login\":\"" + login + "\", \"password\":\"" + password + "\"}")
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    public void loginWithWrongPasswordFails() {
        given()
                .header("Content-type", "application/json")
                .body("{\"login\":\"" + login + "\", \"password\":\"wrong\"}")
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", containsString("Учетная запись не найдена"));
    }

    @Test
    public void loginWithoutLoginFieldFails() {
        given()
                .header("Content-type", "application/json")
                .body("{\"password\":\"" + password + "\"}")
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для входа"));
    }

    @After
    public void tearDown() {
        deleteCourier(courierId);
    }
}
