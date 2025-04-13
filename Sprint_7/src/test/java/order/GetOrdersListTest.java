package order;

import base.BaseTest;
import io.qameta.allure.Step;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetOrdersListTest extends BaseTest {

    @Step("Получение списка заказов")
    @Test
    public void getOrdersList() {
        given()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .body("orders", not(empty()))
                .body("orders[0].id", notNullValue());
    }
}
