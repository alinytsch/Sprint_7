package courier;

import api.CourierApi;
import base.BaseTest;
import io.restassured.response.Response;
import model.Courier;
import model.LoginCredentials;
import org.junit.*;

import java.util.Random;

import static org.hamcrest.Matchers.*;

public class CourierLoginTest extends BaseTest {

    private static final String PASSWORD = "1234";
    private static final String FIRST_NAME = "Naruto";

    private String login;
    private int courierId;
    private CourierApi courierApi;

    public String generateLogin() {
        return "courier" + new Random().nextInt(999999);
    }

    @Before
    public void setUp() {
        courierApi = new CourierApi();
        login = generateLogin();
        Courier courier = new Courier(login, PASSWORD, FIRST_NAME);
        courierApi.createCourier(courier).then().statusCode(201);
        courierId = courierApi.loginCourier(new LoginCredentials(login, PASSWORD))
                .then().statusCode(200).extract().path("id");
    }

    @Test
    public void courierCanLogin() {
        LoginCredentials credentials = new LoginCredentials(login, PASSWORD);
        courierApi.loginCourier(credentials)
                .then().statusCode(200).body("id", notNullValue());
    }

    @Test
    public void loginWithWrongPasswordFails() {
        LoginCredentials credentials = new LoginCredentials(login, "wrong");
        courierApi.loginCourier(credentials)
                .then().statusCode(404)
                .body("message", containsString("Учетная запись не найдена"));
    }

    @Test
    public void loginWithWrongLoginFails() {
        LoginCredentials credentials = new LoginCredentials("wrong" + login, PASSWORD);
        courierApi.loginCourier(credentials)
                .then().statusCode(404)
                .body("message", containsString("Учетная запись не найдена"));
    }

    @Test
    public void loginWithoutLoginFails() {
        LoginCredentials credentials = new LoginCredentials(null, PASSWORD);
        courierApi.loginCourier(credentials)
                .then().statusCode(400)
                .body("message", containsString("Недостаточно данных для входа"));
    }

    @Test
    public void loginWithoutPasswordFails() {
        LoginCredentials credentials = new LoginCredentials(login, null);
        courierApi.loginCourier(credentials)
                .then().statusCode(400)
                .body("message", containsString("Недостаточно данных для входа"));
    }

    @After
    public void tearDown() {
        courierApi.deleteCourier(courierId).then().statusCode(200);
    }
}
