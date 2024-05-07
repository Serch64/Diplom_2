package site.nomoreparties.stellarburgers.steps;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

public class GeneralSteps {
    @Step("Включение логов")
    public void turnOnLogs() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
    @Step("Выключение логов после прогона теста")
    public void turnOffLogs() {
        RestAssured.reset();
    }



}
