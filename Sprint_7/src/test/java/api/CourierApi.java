package api;

import io.restassured.response.Response;
import model.Courier;
import model.LoginCredentials;

import static io.restassured.RestAssured.given;

public class CourierApi {

    public Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier");
    }

    public Response loginCourier(LoginCredentials credentials) {
        return given()
                .header("Content-type", "application/json")
                .body(credentials)
                .post("/api/v1/courier/login");
    }

    public Response deleteCourier(int id) {
        return given()
                .delete("/api/v1/courier/" + id);
    }
}
