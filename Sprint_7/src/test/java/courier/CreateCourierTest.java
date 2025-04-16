package courier;

import api.CourierApi;
import base.BaseTest;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class CreateCourierTest extends BaseTest {

    private final CourierApi courierApi = new CourierApi();
    private Courier testCourier;
    private int courierId;

    @Before
    public void init() {
        testCourier = new Courier("login" + System.currentTimeMillis(), "1234", "TestName");
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierApi.deleteCourier(courierId);
        }
    }

    @Test
    @Description("Успешное создание курьера")
    public void createCourierSuccess() {
        Response createResponse = courierApi.createCourier(testCourier);
        createResponse.then().statusCode(201).body("ok", is(true));

        Response loginResponse = courierApi.loginCourier(
                new model.LoginCredentials(testCourier.getLogin(), testCourier.getPassword()));
        courierId = loginResponse.then().statusCode(200).extract().path("id");
    }
}