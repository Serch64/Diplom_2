package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.steps.GeneralSteps;
import site.nomoreparties.stellarburgers.steps.UserSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static site.nomoreparties.stellarburgers.constants.UserTestData.EMAIL;
import static site.nomoreparties.stellarburgers.constants.UserTestData.NAME;

@DisplayName("Логин пользователя POST: /api/auth/login/")
public class LoginUserTest {
    private static final String UNAUTHORIZED_EXPECTED_MESSAGE = "email or password are incorrect";
    private GeneralSteps generalSteps = new GeneralSteps();
    private UserSteps userSteps = new UserSteps();
    @Before
    public void setUp() {
        generalSteps.turnOnLogs();
        userSteps.createUserClient();
    }
    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("Проверка что пользователь может авторизоваться")
    public void loginUser() {
        userSteps.createUser();
      ValidatableResponse response = userSteps.login().assertThat().body("success", equalTo(true))
                 .body("accessToken", notNullValue())
                 .body("refreshToken", notNullValue())
                 .body("user.email", equalTo(EMAIL))
                 .body("user.name", equalTo(NAME))
                 .and()
                 .statusCode(SC_OK);
        userSteps.accessToken = userSteps.getAccessToken(response);
    }
    @Test
    @DisplayName("Логин с неверным логином и паролем")
    @Description("Проверка что пользователь не может авторизоваться с неверным логином и паролем")
    public void loginInvalidUser() {
        userSteps.createUnknownUser();
        ValidatableResponse response = userSteps.login().assertThat().body("success", equalTo(false))
                .body("message", equalTo(UNAUTHORIZED_EXPECTED_MESSAGE))
                .and()
                .statusCode(SC_UNAUTHORIZED);
        userSteps.accessToken = userSteps.getAccessToken(response);
    }
    @After
    public void deleteTestDataAndTurnOffLogs() {
        userSteps.deleteUser();
        generalSteps.turnOffLogs();
    }
}
