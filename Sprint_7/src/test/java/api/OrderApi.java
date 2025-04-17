package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;

import static io.restassured.RestAssured.given;

public class OrderApi {

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return given().header("Content-type", "application/json").body(order).post("/api/v1/orders");
    }

    @Step("Получение списка заказов")
    public Response getOrdersList() {
        return given().get("/api/v1/orders");
    }
}
