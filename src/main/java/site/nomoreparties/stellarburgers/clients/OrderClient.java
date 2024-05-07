package site.nomoreparties.stellarburgers.clients;

import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.pojo.Ingredients;

import static io.restassured.RestAssured.given;

public class OrderClient extends Spec{
    private static final String GET_INGREDIENTS_ENDPOINT = "/api/ingredients/";
    private static final String CREATE_ORDER_ENDPOINT = "/api/orders/";
    private static final String AUTHORIZATION = "Authorization";

    public ValidatableResponse getIngredientClient() {
        return given()
                .spec(getSpec())
                .get(GET_INGREDIENTS_ENDPOINT)
                .then();
    }
    public ValidatableResponse createOrderClientAuth(Ingredients ingredients, String accessToken) {
        return given()
                .spec(getSpec())
                .header(AUTHORIZATION,accessToken)
                .body(ingredients)
                .when()
                .post(CREATE_ORDER_ENDPOINT)
                .then();
    }
    public ValidatableResponse createOrderClientNotAuth(Ingredients ingredients) {
        return given()
                .spec(getSpec())
                .body(ingredients)
                .when()
                .post(CREATE_ORDER_ENDPOINT)
                .then();
    }
}
