package courier;

import base.BaseTest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateCourierTest extends BaseTest {

    private String login;
    private int courierId;

    private final String password = "1234";
    private final String firstName = "Saske";

    @Step("Генерация уникального логина")
    public String generateLogin() {
        return "user" + new Random().nextInt(999999);
    }

    @Step("Создание курьера")
    public Response createCourier(String login, String password, String firstName) {
        return given()
                .header("Content-type", "application/json")
                .body("{\"login\":\"" + login + "\", \"password\":\"" + password + "\", \"firstName\":\"" + firstName + "\"}")
                .when()
                .post("/api/v1/courier");
    }

    @Step("Логин курьера для получения ID")
    public int loginCourier(String login, String password) {
        return given()
                .header("Content-type", "application/json")
                .body("{\"login\":\"" + login + "\", \"password\":\"" + password + "\"}")
                .when()
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

    @Test
    public void courierCanBeCreated() {
        login = generateLogin();
        Response response = createCourier(login, password, firstName);
        response.then().statusCode(201).body("ok", is(true));

        courierId = loginCourier(login, password);
    }

    @Test
    public void cannotCreateDuplicateCourier() {
        login = generateLogin();
        createCourier(login, password, firstName).then().statusCode(201);
        createCourier(login, password, firstName).then().statusCode(409)
                .body("message", containsString("Этот логин уже используется"));
    }

    @Test
    public void createCourierWithoutRequiredFields() {
        given()
                .header("Content-type", "application/json")
                .body("{\"login\":\"" + login + "\"}")
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void teardown() {
        if (courierId != 0) {
            deleteCourier(courierId);
        }
    }
}
