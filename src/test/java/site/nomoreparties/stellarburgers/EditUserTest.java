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

@DisplayName("Изменение данных пользователя PATCH: /api/auth/user/")
public class EditUserTest {

    private static final String UNAUTHORIZED_EXPECTED_MESSAGE = "You should be authorised";
    private GeneralSteps generalSteps = new GeneralSteps();
    private UserSteps userSteps = new UserSteps();
    @Before
    public void setUp() {
        generalSteps.turnOnLogs();
        userSteps.createUserClient();
    }
    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    @Description("Проверка что у авторизованного пользователя можно изменять поля")
    public void editUserAuth() {
        userSteps.createUser();
        ValidatableResponse response = userSteps.login();
        userSteps.accessToken = userSteps.getAccessToken(response);
        userSteps.editAuth().assertThat().body("success", equalTo(true))
                .body("user.email", equalTo(userSteps.fakerEmail))
                .body("user.name", equalTo(userSteps.fakerName))
                .and()
                .statusCode(SC_OK);
    }
    @Test
    @DisplayName("Изменение данных пользователя без авторизацией")
    @Description("Проверка что у неавторизованного пользователя нельзя изменять поля")
    public void editUserNotAuth() {
        userSteps.editNotAuth().assertThat().body("success", equalTo(false))
                .body("message", equalTo(UNAUTHORIZED_EXPECTED_MESSAGE))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }
    @After
    public void deleteTestDataAndTurnOffLogs() {
        userSteps.deleteUser();
        generalSteps.turnOffLogs();
    }
}
