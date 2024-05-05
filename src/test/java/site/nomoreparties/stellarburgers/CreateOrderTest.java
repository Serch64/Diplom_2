package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

@DisplayName("Создание заказа POST: /api/orders/")
public class CreateOrderTest extends Steps {

    private static final String BAD_REQUEST_EXPECTED_MESSAGE = "Ingredient ids must be provided";

    @Before
    public void setUp() {
        turnOnLogs();
        createOrderClient();
    }
    @Test
    @DisplayName("Тест на создание заказа с ингредиентами и с авторизацией")
    @Description("Проверка на создание заказа с авторизацией и ингредиентами")
    public void createNewOrderAuth() {
        createUserClient();
        createUser();
        ValidatableResponse response = login();
        accessToken = getAccessToken(response);
        List<String> twoIngredients = getTwoIngredients();
        createNewOrderAuth(twoIngredients, accessToken).assertThat().body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order",notNullValue())
                .and()
                .statusCode(SC_OK);
    }
    @Test
    @DisplayName("Тест на создание заказа с ингредиентами и без авторизации")
    @Description("Проверка на создание заказа без авторизации и c ингредиентами")
    public void createNewOrderNotAuth() {
        List<String> twoIngredients = getTwoIngredients();
        createNewOrderNotAuth(twoIngredients).assertThat().body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order",notNullValue())
                .and()
                .statusCode(SC_OK);
    }
    @Test
    @DisplayName("Тест на создание заказа без ингредиентов")
    @Description("Проверка на создание заказа без ингредиентов и получение ошибки")
    public void createNewOrderNullIngredients() {
        List<String> nullIngredients = getNullIngredients();
        createNewOrderNotAuth(nullIngredients).assertThat().body("success", equalTo(false))
                .body("message",  equalTo(BAD_REQUEST_EXPECTED_MESSAGE))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }
    @Test
    @DisplayName("Тест на создание заказа с неверным хешем")
    @Description("Проверка на создание заказа неверным хешем и получение ошибки")
    public void createNewOrderInvalidIngredients() {
        List<String> invalidIngredient = getInvalidIngredient();
        createNewOrderNotAuth(invalidIngredient).statusCode(SC_INTERNAL_SERVER_ERROR);
    }
    @After
    public void deleteTestDataAndTurnOffLogs() {
        deleteUser();
        turnOffLogs();
    }
}
