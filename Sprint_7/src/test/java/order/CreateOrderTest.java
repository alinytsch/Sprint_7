package order;

import api.OrderApi;
import base.BaseTest;
import model.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest extends BaseTest {

    private final OrderApi orderApi = new OrderApi();
    private final String[] colors;

    public CreateOrderTest(String[] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Цвета: {0}")
    public static Collection<Object[]> colorData() {
        return Arrays.asList(new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        });
    }

    @Test
    public void createOrderWithColors() {
        Order order = new Order(
                "Kakashi", "Hatake", "Konoha 17",
                5, "+7 800 555 35 35",
                3, "2025-04-14", "No comments", colors
        );

        orderApi.createOrder(order)
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}
