package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@DisplayName("Получение заказов конкретного пользователя GET: /api/orders/")
public class GetOrdersTest extends Steps{

    private static final String UNAUTHORIZED_EXPECTED_MESSAGE = "You should be authorised";

    @Before
    public void setUp() {
        turnOnLogs();
        createOrderClient();
        createGetOrdersClient();
    }
    @Test
    @DisplayName("Тест на получение заказов конкретного пользователя с авторизацией")
    @Description("Проверка на получение заказов конкретного пользователя с авторизацией")
    public void getOrdersAuth() {
        createUserClient();
        createUser();
        ValidatableResponse response = login();
        accessToken = getAccessToken(response);
        List<String> twoIngredients = getTwoIngredients();
        createNewOrderAuth(twoIngredients, accessToken);
        getOrdersWithAuth(accessToken).assertThat().body("success", equalTo(true))
                .body("orders", notNullValue())
                .body("total",notNullValue())
                .body("totalToday",notNullValue())
                .and()
                .statusCode(SC_OK);
    }
    @Test
    @DisplayName("Тест на получение заказов конкретного пользователя без авторизации")
    @Description("Проверка на получение заказов конкретного пользователя без авторизации")
    public void getOrdersNotAuth() {
        createUserClient();
        createUser();
        ValidatableResponse response = login();
        accessToken = getAccessToken(response);
        List<String> twoIngredients = getTwoIngredients();
        createNewOrderAuth(twoIngredients, accessToken);
        getOrdersWithoutAuth().assertThat().body("success", equalTo(false))
                .body("message",  equalTo(UNAUTHORIZED_EXPECTED_MESSAGE))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }
    @After
    public void deleteTestDataAndTurnOffLogs() {
        deleteUser();
        turnOffLogs();
    }
}
