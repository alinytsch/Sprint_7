package courier;

import api.CourierApi;
import base.BaseTest;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.Courier;
import model.LoginCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest extends BaseTest {

    private static final String PASSWORD = "1234";
    private static final String FIRST_NAME = "Naruto";

    private CourierApi courierApi;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courierApi = new CourierApi();
        String login = "log" + System.currentTimeMillis();
        courier = new Courier(login, PASSWORD, FIRST_NAME);
        courierApi.createCourier(courier);

        Response loginResponse = courierApi.loginCourier(new LoginCredentials(courier.getLogin(), courier.getPassword()));
        courierId = loginResponse.then().extract().path("id");
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierApi.deleteCourier(courierId);
        }
    }

    @Test
    @Description("Курьер может войти с корректными данными")
    public void courierCanLoginSuccessfully() {
        Response response = courierApi.loginCourier(new LoginCredentials(courier.getLogin(), courier.getPassword()));
        response.then().statusCode(200).body("id", notNullValue());
    }

    @Test
    @Description("Логин без логина")
    public void loginWithoutLoginFails() {
        Response response = courierApi.loginCourier(new LoginCredentials(null, PASSWORD));
        response.then().statusCode(400).body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Description("Логин без пароля")
    public void loginWithoutPasswordFails() {
        Response response = courierApi.loginCourier(new LoginCredentials(courier.getLogin(), null));
        response.then().statusCode(400).body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Description("Логин с неправильным паролем")
    public void loginWithWrongPasswordFails() {
        Response response = courierApi.loginCourier(new LoginCredentials(courier.getLogin(), "wrongpass"));
        response.then().statusCode(404).body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Description("Логин с неправильным логином")
    public void loginWithWrongLoginFails() {
        Response response = courierApi.loginCourier(new LoginCredentials("wronglogin", PASSWORD));
        response.then().statusCode(404).body("message", equalTo("Учетная запись не найдена"));
    }
}
