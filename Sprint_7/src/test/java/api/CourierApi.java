package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Courier;
import model.LoginCredentials;

import static io.restassured.RestAssured.given;

public class CourierApi {

    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }

    @Step("Логин курьера")
    public Response loginCourier(LoginCredentials credentials) {
        return given()
                .header("Content-Type", "application/json")
                .body(credentials)
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Удаление курьера по id")
    public Response deleteCourier(int id) {
        return given()
                .when()
                .delete(COURIER_PATH + "/" + id);
    }
}