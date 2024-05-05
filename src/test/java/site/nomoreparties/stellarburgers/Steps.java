package site.nomoreparties.stellarburgers;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.clients.*;
import site.nomoreparties.stellarburgers.pojo.*;

import java.util.ArrayList;
import java.util.List;

import static site.nomoreparties.stellarburgers.constants.UserTestData.*;

public class Steps {
    protected UserClient userClient;
    protected OrderClient orderClient;
    protected GetOrdersClient getOrdersClient;
    protected User user;
    protected String accessToken;
    protected Faker faker = new Faker();
    protected String fakerEmail;
    protected String fakerPassword;
    protected String fakerName;

    @Step("Включение логов")
    public void turnOnLogs() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
    @Step("Создание клиента пользователя")
    public void createUserClient() {
        userClient = new UserClient();
    }
    @Step("Создание пользователя")
    public ValidatableResponse createUser() {
        user = new User(EMAIL, PASSWORD, NAME);
        return userClient.createUserClient(user);
    }
    @Step("Создание пользователя без обязательного поля")
    public ValidatableResponse createInvalidUser() {
        user = new User(EMAIL, PASSWORD, NAME);
        user.setEmail(null);
        return userClient.createUserClient(user);
    }
    @Step("Создание пользователя с логином и паролем которого нет в системе")
    public void createUnknownUser() {
        fakerEmail = faker.internet().emailAddress();
        fakerPassword = faker.internet().password();
        user = new User(fakerEmail, fakerPassword, NAME);
    }
    @Step("Получение токена")
    public String getAccessToken(ValidatableResponse response) {
        return response.extract().jsonPath().getString("accessToken");
    }
    @Step("Авторизация пользователя в системе")
    public ValidatableResponse login() {
        return userClient.loginUserClient(UserCreds.from(user));
    }
    @Step("Изменение пользователя с авторизацией")
    public ValidatableResponse editAuth() {
        fakerEmail = faker.internet().emailAddress();
        fakerName = faker.name().firstName();
        UserData userData = new UserData(fakerEmail, fakerName);
        return userClient.editUserClientAuth(userData, accessToken);
    }
    @Step("Изменение пользователя без авторизацией")
    public ValidatableResponse editNotAuth() {
        fakerEmail = faker.internet().emailAddress();
        fakerName = faker.name().firstName();
        UserData userData = new UserData(fakerEmail, fakerName);
        return userClient.editUserClientNotAuth(userData);
    }
    @Step("Удаление пользователя")
    public void deleteUser() {
        if (accessToken != null) {
            userClient.deleteUserClient(accessToken);
        }
    }
    @Step("Создание клиента заказа")
    public void createOrderClient() {
        orderClient = new OrderClient();
    }
    @Step("Получение двух ингредиентов")
    public List<String> getTwoIngredients() {
        List<String> twoIngredients = orderClient.getIngredientClient().extract().jsonPath().getList("data._id");
        return twoIngredients.subList(0,2);
    }
    @Step("Получение ноль ингредиентов")
    public List<String> getNullIngredients() {
        return new ArrayList<>();
    }
    @Step("Получение ингредиентов с неверным хешем")
    public List<String> getInvalidIngredient() {
        List<String> invalidIngredient = new ArrayList<>();
        invalidIngredient.add("sfsdafasdf1231231");
        return invalidIngredient;
    }
    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createNewOrderAuth(List<String> ingredients, String accessToken) {
        Ingredients newIngredients = new Ingredients(ingredients);
        return orderClient.createOrderClientAuth(newIngredients, accessToken);
    }
    @Step("Создание заказа без авторизации")
    public ValidatableResponse createNewOrderNotAuth(List<String> ingredients) {
        Ingredients newIngredients = new Ingredients(ingredients);
        return orderClient.createOrderClientNotAuth(newIngredients);
    }
    @Step("Создание клиента получения заказов")
    public void createGetOrdersClient() {
        getOrdersClient = new GetOrdersClient();
    }
    @Step("Получение заказов с авторизаций")
    public ValidatableResponse getOrdersWithAuth(String accessToken) {
        return getOrdersClient.getOrdersClientAuth(accessToken);
    }
    @Step("Получение заказов без авторизации")
    public ValidatableResponse getOrdersWithoutAuth() {
        return getOrdersClient.getOrdersClientNotAuth();
    }
    @Step("Выключение логов после прогона теста")
    public void turnOffLogs() {
        RestAssured.reset();
    }
}
