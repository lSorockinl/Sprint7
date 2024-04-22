import clients.CourierClient;
import clients.OrderClient;
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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private Integer track;
    private String orderBaseUri = "/api/v1/orders";
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;

    public CreateOrderTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters(name = "Выбранный цвет/Выбранные цвета: {8}")
    public static Object[][] createOrder() {
        return new Object[][] {
                {"Test", "Test", "123 Test", "Test", "1234567890", 3, "2024-04-04", "Test comment", new String[]{"BLACK"}},
                {"Test2", "TestTest", "456 test", "Test", "0987654321", 2, "2024-04-04", "Test2 comment", new String[]{"GREY"}},
                {"Test3", "Test", "789 test", "Test", "5555555555", 2, "2024-04-04", "test comment", new String[]{"BLACK", "GREY"}},
                {"Test4", "TestTest", "321 Testtest", "Test", "7777777777", 3, "2024-04-04", "test", new String[]{}}
        };
    }
    @Before
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
    @Test
    @DisplayName("Успешное создание заказа при выборе любого цвета(BLACK or GREY), при выборе двух цветов и при отсутствии выбора цвета")
    @Description("Проверка статуса кода и присвоения track заказу")
    public void createOrderBeSuccessful() {
        track =    given()
                .contentType(ContentType.JSON)
                .baseUri("http://qa-scooter.praktikum-services.ru/")
                .body(createOrder())
                .when()
                .post(orderBaseUri)
                .then()
                .statusCode(201)
                .assertThat().body("track", Matchers.notNullValue())
                .extract().jsonPath().get("track");
    }

    @After
    public void tearDown() {
        if (track != null) {
            given()
                    .contentType(ContentType.JSON)
                    .baseUri("http://qa-scooter.praktikum-services.ru/")
                    .pathParam("track", track)
                    .when()
                    .put(orderBaseUri + "/cancel/{track}");
        }
    }
}