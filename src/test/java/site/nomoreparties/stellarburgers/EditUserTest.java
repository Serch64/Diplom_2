package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Изменение данных пользователя PATCH: /api/auth/user/")
public class EditUserTest extends Steps {

    private static final String UNAUTHORIZED_EXPECTED_MESSAGE = "You should be authorised";

    @Before
    public void setUp() {
        turnOnLogs();
        createUserClient();
    }
    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    @Description("Проверка что у авторизованного пользователя можно изменять поля")
    public void editUserAuth() {
        createUser();
        ValidatableResponse response = login();
        accessToken = getAccessToken(response);
        editAuth().assertThat().body("success", equalTo(true))
                .body("user.email", equalTo(fakerEmail))
                .body("user.name", equalTo(fakerName))
                .and()
                .statusCode(SC_OK);
    }
    @Test
    @DisplayName("Изменение данных пользователя без авторизацией")
    @Description("Проверка что у неавторизованного пользователя нельзя изменять поля")
    public void editUserNotAuth() {
        editNotAuth().assertThat().body("success", equalTo(false))
                .body("message", equalTo(UNAUTHORIZED_EXPECTED_MESSAGE))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }
    @After
    public void deleteTestDataAndTurnOffLogs() {
        deleteUser();
        turnOffLogs();
    }
}
