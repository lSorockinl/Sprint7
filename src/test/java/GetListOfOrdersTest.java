import clients.CourierClient;
import clients.OrderClient;
import dataprovider.CourierProvider;
import dataprovider.OrderProvider;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import request.*;
import static io.restassured.RestAssured.given;

public class GetListOfOrdersTest {
    private OrderClient orderClient = new OrderClient();
    private CourierClient courierClient = new CourierClient();
    private int id;
    private int courierId;
    private int t;

    @Before
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    @DisplayName("Успешное получение списка заказов без указания id курьера")
    @Description("Проверка статуса кода и отсутствия id курьера в заказах")
    public void getListOfOrdersWithoutCourierId() {
        //создать заказ
        CreateOrderRequest createOrderRequest = OrderProvider.getRandomCreateOrderRequest();
        t = orderClient.create(createOrderRequest)
                .extract().jsonPath().get("track");
        //проверить , что получен список заказов, courierId = null
        GetListOfOrderRequest getListOfOrderRequest = new GetListOfOrderRequest();
        orderClient.getList(getListOfOrderRequest)
                .statusCode(200)
                .assertThat().body("order.courierId", Matchers.equalTo(null));

    }

    @Test
    @DisplayName("Успешное получение списка заказов существующего курьера")
    @Description("Проверка статуса кода и наличия заказа у курьера")
    public void getListOfOrdersWithCorrectCourierId() {
        //создать курьера
        CreateCourierRequest createCourierRequest = CourierProvider.getRandomCreateCourierRequest();
        courierClient.create(createCourierRequest);
        //залогиниться и получить CourierId
        LoginCourierRequest loginCourierRequest = LoginCourierRequest.from(createCourierRequest);
        courierId = courierClient.login(loginCourierRequest)
                .extract().jsonPath().get("id");

        //создать заказ
        CreateOrderRequest createOrderRequest = OrderProvider.getRandomCreateOrderRequest();
        t = orderClient.create(createOrderRequest)
                .extract().jsonPath().get("track");
        //получить заказ по его track и забрать id
        id = orderClient.getOrderBytrack(t)
                .extract().jsonPath().get("order.id");
        //принять заказ
        orderClient.accept(id, courierId);
        //получить список заказов по курьеру id
        orderClient.getListWithCourierId(courierId)
                .statusCode(200)
                .body("orders", Matchers.notNullValue());
    }

    @After
    public void tearDown() {
        given()
                .contentType(ContentType.JSON)
                .baseUri("http://qa-scooter.praktikum-services.ru/")
                .pathParam("track", t)
                .when()
                .put("/api/v1/orders/cancel/{track}");
    }
}