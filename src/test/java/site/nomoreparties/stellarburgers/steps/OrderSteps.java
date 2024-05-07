package site.nomoreparties.stellarburgers.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.clients.OrderClient;
import site.nomoreparties.stellarburgers.pojo.Ingredients;

import java.util.ArrayList;
import java.util.List;

public class OrderSteps {
    private static final String INVALID_INGREDIENT_HASH = "sfsdafasdf1231231";
    private OrderClient orderClient;

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
        invalidIngredient.add(INVALID_INGREDIENT_HASH);
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
}
