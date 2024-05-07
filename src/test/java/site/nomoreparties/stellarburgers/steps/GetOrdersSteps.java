package site.nomoreparties.stellarburgers.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.clients.GetOrdersClient;

public class GetOrdersSteps {
    private GetOrdersClient getOrdersClient;
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
}
