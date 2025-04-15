package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Courier;
import model.LoginCredentials;

import static io.restassured.RestAssured.given;

public class CourierApi {

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given().header("Content-type", "application/json").body(courier).post("/api/v1/courier");
    }

    @Step("Логин курьера")
    public Response loginCourier(LoginCredentials credentials) {
        return given().header("Content-type", "application/json").body(credentials).post("/api/v1/courier/login");
    }

    @Step("Удаление курьера по id")
    public Response deleteCourier(int id) {
        return given().delete("/api/v1/courier/" + id);
    }
}
