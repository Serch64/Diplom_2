package site.nomoreparties.stellarburgers.clients;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class GetOrdersClient extends Spec{
    private static final String GET_ORDERS_ENDPOINT = "/api/orders/";
    private static final String AUTHORIZATION = "Authorization";

    public ValidatableResponse getOrdersClientAuth(String accessToken) {
        return given()
                .spec(getSpec())
                .header(AUTHORIZATION,accessToken)
                .get(GET_ORDERS_ENDPOINT)
                .then();
    }
    public ValidatableResponse getOrdersClientNotAuth() {
        return given()
                .spec(getSpec())
                .get(GET_ORDERS_ENDPOINT)
                .then();
    }
}
