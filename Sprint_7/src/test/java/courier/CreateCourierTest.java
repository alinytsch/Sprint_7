package courier;

import api.CourierApi;
import base.BaseTest;
import model.Courier;
import model.LoginCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.Matchers.*;

public class CreateCourierTest extends BaseTest {

    private static final String PASSWORD = "1234";
    private static final String FIRST_NAME = "Saske";

    private String login;
    private int courierId;
    private CourierApi courierApi;

    public String generateLogin() {
        return "user" + new Random().nextInt(999999);
    }

    @Before
    public void init() {
        courierApi = new CourierApi();
    }

    @Test
    public void courierCanBeCreated() {
        login = generateLogin();
        Courier courier = new Courier(login, PASSWORD, FIRST_NAME);
        courierApi.createCourier(courier).then().statusCode(201).body("ok", is(true));
        courierId = courierApi.loginCourier(new LoginCredentials(login, PASSWORD))
                .then().statusCode(200).extract().path("id");
    }

    @Test
    public void cannotCreateDuplicateCourier() {
        login = generateLogin();
        Courier courier = new Courier(login, PASSWORD, FIRST_NAME);
        courierApi.createCourier(courier).then().statusCode(201);
        courierApi.createCourier(courier)
                .then().statusCode(409)
                .body("message", containsString("Этот логин уже используется"));
    }

    @Test
    public void createCourierWithoutRequiredFields() {
        Courier courier = new Courier(login, null, null);
        courierApi.createCourier(courier)
                .then().statusCode(400)
                .body("message", containsString("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void teardown() {
        if (courierId != 0) {
            courierApi.deleteCourier(courierId).then().statusCode(200);
        }
    }
}
