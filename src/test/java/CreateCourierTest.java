import clients.CourierClient;
import dataprovider.CourierProvider;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import request.CreateCourierRequest;
import request.LoginCourierRequest;

public class CreateCourierTest {
    private CourierClient courierClient = new CourierClient();
    private Integer id;

    @Before
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    @DisplayName("Успешное создание нового курьера")
    @Description("Проверка статуса кода, тела при создании курьера с корректными данными")
    public void courierShouldBeCreated() {
        CreateCourierRequest createCourierRequest = CourierProvider.getRandomCreateCourierRequest();

        //создание
        courierClient.create(createCourierRequest)
                .statusCode(201)
                .body("ok", Matchers.equalTo(true));

        //логин
        LoginCourierRequest loginCourierRequest = LoginCourierRequest.from(createCourierRequest);

        id = courierClient.login(loginCourierRequest)
                .statusCode(200)
                .extract().jsonPath().get("id");
    }

    @Test
    @DisplayName("Курьер не создается, если при создании указан уже существующий логин")
    @Description("Проверка статуса кода и ошибки в теле ответа")
    public void sameCourierDontBeCreated() {
        CreateCourierRequest createCourierRequest = CourierProvider.getRandomCreateCourierRequest();

        //создание
        courierClient.create(createCourierRequest);

        courierClient.create(createCourierRequest)
                .statusCode(409)
                .body("message", Matchers.equalTo("Этот логин уже используется. Попробуйте другой."));
        //логин
        LoginCourierRequest loginCourierRequest = LoginCourierRequest.from(createCourierRequest);

        id = courierClient.login(loginCourierRequest)
                .extract().jsonPath().get("id");
    }

    @Test
    @DisplayName("Успешное создание курьера без ввода firstName")
    @Description("Проверка статуса кода и тела при создании курьера без firstName")
    public void courierBeCreatedWithoutFirstName() {
        CreateCourierRequest createCourierRequest = CourierProvider.getRandomWithoutFirstNameCourierRequest();
        //создание
        courierClient.create(createCourierRequest)
                .statusCode(201)
                .body("ok", Matchers.equalTo(true));

        //логин
        LoginCourierRequest loginCourierRequest = LoginCourierRequest.from(createCourierRequest);

        id = courierClient.login(loginCourierRequest)
                .statusCode(200)
                .extract().jsonPath().get("id");
    }

    @Test
    @DisplayName("Курьер не создается , если не указать логин при создании")
    @Description("Проверка статуса кода и ошибки в теле ответа")
    public void courierDontBeCreatedWithoutLogin() {
        CreateCourierRequest createCourierRequest = CourierProvider.getRandomWithoutLoginCourierRequest();

        //создание
        courierClient.create(createCourierRequest)
                .statusCode(400)
                .body("message", Matchers.equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Курьер не создается, если не указать пароль при создании")
    @Description("Проверка статуса кода и ошибки в теле ответа")
    public void courierDontBeCreatedWithoutPassword() {
        CreateCourierRequest createCourierRequest = CourierProvider.getRandomWithoutPasswordCourierRequest();

        //создание
        courierClient.create(createCourierRequest)
                .statusCode(400)
                .body("message", Matchers.equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void tearDown() {
        if (id != null) {
            courierClient.delete(id)
                    .statusCode(200);
        }
    }
}