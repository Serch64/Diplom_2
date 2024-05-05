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
import static site.nomoreparties.stellarburgers.constants.UserTestData.*;

@DisplayName("Создание пользователя POST: /api/auth/register/")
public class CreateUserTest extends Steps {
    private static final String FORBIDDEN_SAME_USER_EXPECTED_MESSAGE = "User already exists";
    private static final String FORBIDDEN_USER_MISS_FIELD_EXPECTED_MESSAGE = "Email, password and name are required fields";

    @Before
    public void setUp() {
        turnOnLogs();
        createUserClient();
    }
    @Test
    @DisplayName("Тест на создание пользователя")
    @Description("Проверка на создание уникального пользователя")
    public void createNewUser() {
         ValidatableResponse response = createUser().assertThat().body("success", equalTo(true))
                .body("user.email", equalTo(EMAIL))
                .body("user.name", equalTo(NAME))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .and()
                .statusCode(SC_OK);
                accessToken = getAccessToken(response);
    }
    @Test
    @DisplayName("Тест на создание пользователя, который уже зарегистрирован")
    @Description("Проверка на создание пользователя, который уже зарегистрирован и получение ошибки")
    public void createSameUser() {
        ValidatableResponse response = createUser();
        accessToken = getAccessToken(response);
        createUser().assertThat().body("success", equalTo(false))
                .body("message",  equalTo(FORBIDDEN_SAME_USER_EXPECTED_MESSAGE))
                .and()
                .statusCode(SC_FORBIDDEN);
    }
    @Test
    @DisplayName("Тест на создание пользователя без обязательного поля")
    @Description("Проверка на создание пользователя без обязательного поля и получение ошибки")
    public void createUserMissField() {
        ValidatableResponse response = createInvalidUser().assertThat().body("success", equalTo(false))
                .body("message",  equalTo(FORBIDDEN_USER_MISS_FIELD_EXPECTED_MESSAGE))
                .and()
                .statusCode(SC_FORBIDDEN);
        accessToken = getAccessToken(response);
    }
    @After
    public void deleteTestDataAndTurnOffLogs() {
        deleteUser();
        turnOffLogs();
    }
}
