package order;

import base.BaseTest;
import io.qameta.allure.Step;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest extends BaseTest {

    private final String[] color;

    public CreateOrderTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвет заказа: {0}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        });
    }

    @Step("Создание тела заказа с цветами: {0}")
    public Map<String, Object> buildOrderRequest(String[] color) {
        Map<String, Object> body = new HashMap<>();
        body.put("firstName", "Naruto");
        body.put("lastName", "Uchiha");
        body.put("address", "Konoha, 142 apt.");
        body.put("metroStation", 4);
        body.put("phone", "+7 800 355 35 35");
        body.put("rentTime", 5);
        body.put("deliveryDate", "2020-06-06");
        body.put("comment", "Saske, come back to Konoha");
        body.put("color", color); // передаём массив корректно
        return body;
    }

    @Step("Создание заказа через API")
    @Test
    public void createOrderWithDifferentColors() {
        Map<String, Object> orderRequest = buildOrderRequest(color);

        given()
                .header("Content-type", "application/json")
                .body(orderRequest)
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}
