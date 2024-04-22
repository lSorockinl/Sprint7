package clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import request.*;
import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {
    private final String orderBaseUri = "/api/v1/orders";

    @Step("Создать заказ")
    public ValidatableResponse create(CreateOrderRequest createOrderRequest) {
        return  given()
                .spec(getSpec())
                .body(createOrderRequest)
                .when()
                .post(orderBaseUri)
                .then();
    }

    @Step("Получить список заказов без ввода курьера id")
    public ValidatableResponse getList(GetListOfOrderRequest getListOfOrderRequest) {
        return  given()
                .spec(getSpec())
                .body(getListOfOrderRequest)
                .when()
                .get(orderBaseUri)
                .then();
    }

    @Step("Получить список заказов по курьеру")
    public ValidatableResponse getListWithCourierId(int courierId) {
        return given()
                .spec(getSpec())
                .pathParam("courierId", courierId)
                .when()
                .get(orderBaseUri + "?courierId={courierId}")
                .then();
    }

    @Step("Принять заказ")
    public ValidatableResponse accept(int id, int courierId) {
        return given()
                .spec(getSpec())
                .pathParam("id", id)
                .pathParam("courierId", courierId)
                .when()
                .put(orderBaseUri + "/accept/{id}?courierId={courierId}")
                .then();
    }

    @Step("Найти заказ по введенному треку")
    public ValidatableResponse getOrderBytrack(int t) {
        return given()
                .spec(getSpec())
                .pathParam("t", t)
                .when()
                .get(orderBaseUri + "/track?t={t}")
                .then();
    }
}
