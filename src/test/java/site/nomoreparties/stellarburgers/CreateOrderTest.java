package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.steps.GeneralSteps;
import site.nomoreparties.stellarburgers.steps.OrderSteps;
import site.nomoreparties.stellarburgers.steps.UserSteps;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

@DisplayName("Создание заказа POST: /api/orders/")
public class CreateOrderTest {
    private static final String BAD_REQUEST_EXPECTED_MESSAGE = "Ingredient ids must be provided";
    private GeneralSteps generalSteps = new GeneralSteps();
    private UserSteps userSteps = new UserSteps();
    private OrderSteps orderSteps = new OrderSteps();

    @Before
    public void setUp() {
        generalSteps.turnOnLogs();
        orderSteps.createOrderClient();
    }
    @Test
    @DisplayName("Тест на создание заказа с ингредиентами и с авторизацией")
    @Description("Проверка на создание заказа с авторизацией и ингредиентами")
    public void createNewOrderAuth() {
        userSteps.createUserClient();
        userSteps.createUser();
        ValidatableResponse response = userSteps.login();
        userSteps.accessToken = userSteps.getAccessToken(response);
        List<String> twoIngredients =  orderSteps.getTwoIngredients();
        orderSteps.createNewOrderAuth(twoIngredients, userSteps.accessToken).assertThat().body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order",notNullValue())
                .and()
                .statusCode(SC_OK);
    }
    @Test
    @DisplayName("Тест на создание заказа с ингредиентами и без авторизации")
    @Description("Проверка на создание заказа без авторизации и c ингредиентами")
    public void createNewOrderNotAuth() {
        List<String> twoIngredients =  orderSteps.getTwoIngredients();
        orderSteps.createNewOrderNotAuth(twoIngredients).assertThat().body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order",notNullValue())
                .and()
                .statusCode(SC_OK);
    }
    @Test
    @DisplayName("Тест на создание заказа без ингредиентов")
    @Description("Проверка на создание заказа без ингредиентов и получение ошибки")
    public void createNewOrderNullIngredients() {
        List<String> nullIngredients =  orderSteps.getNullIngredients();
        orderSteps.createNewOrderNotAuth(nullIngredients).assertThat().body("success", equalTo(false))
                .body("message",  equalTo(BAD_REQUEST_EXPECTED_MESSAGE))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }
    @Test
    @DisplayName("Тест на создание заказа с неверным хешем")
    @Description("Проверка на создание заказа неверным хешем и получение ошибки")
    public void createNewOrderInvalidIngredients() {
        List<String> invalidIngredient =  orderSteps.getInvalidIngredient();
        orderSteps.createNewOrderNotAuth(invalidIngredient).statusCode(SC_INTERNAL_SERVER_ERROR);
    }
    @After
    public void deleteTestDataAndTurnOffLogs() {
        userSteps.deleteUser();
        generalSteps.turnOffLogs();
    }
}
