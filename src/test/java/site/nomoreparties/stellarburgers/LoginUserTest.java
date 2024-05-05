package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static site.nomoreparties.stellarburgers.constants.UserTestData.EMAIL;
import static site.nomoreparties.stellarburgers.constants.UserTestData.NAME;

@DisplayName("Логин пользователя POST: /api/auth/login/")
public class LoginUserTest extends Steps {

    private static final String UNAUTHORIZED_EXPECTED_MESSAGE = "email or password are incorrect";

    @Before
    public void setUp() {
        turnOnLogs();
        createUserClient();
    }
    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("Проверка что пользователь может авторизоваться")
    public void loginUser() {
      createUser();
      ValidatableResponse response = login().assertThat().body("success", equalTo(true))
                 .body("accessToken", notNullValue())
                 .body("refreshToken", notNullValue())
                 .body("user.email", equalTo(EMAIL))
                 .body("user.name", equalTo(NAME))
                 .and()
                 .statusCode(SC_OK);
        accessToken = getAccessToken(response);
    }
    @Test
    @DisplayName("Логин с неверным логином и паролем")
    @Description("Проверка что пользователь не может авторизоваться с неверным логином и паролем")
    public void loginInvalidUser() {
        createUnknownUser();
        ValidatableResponse response = login().assertThat().body("success", equalTo(false))
                .body("message", equalTo(UNAUTHORIZED_EXPECTED_MESSAGE))
                .and()
                .statusCode(SC_UNAUTHORIZED);
        accessToken = getAccessToken(response);
    }
    @After
    public void deleteTestDataAndTurnOffLogs() {
        deleteUser();
        turnOffLogs();
    }
}
