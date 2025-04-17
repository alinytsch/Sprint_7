package order;

import api.OrderApi;
import base.BaseTest;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersListTest extends BaseTest {

    private final OrderApi orderApi = new OrderApi();

    @Test
    public void ordersListShouldReturnNotNullOrders() {
        orderApi.getOrdersList()
                .then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}
