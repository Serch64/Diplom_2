package site.nomoreparties.stellarburgers.clients;

import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.pojo.User;
import site.nomoreparties.stellarburgers.pojo.UserCreds;
import site.nomoreparties.stellarburgers.pojo.UserData;

import static io.restassured.RestAssured.given;

public class UserClient extends Spec{
    private static final String CREATE_USER_ENDPOINT = "/api/auth/register/";
    private static final String DELETE_USER_ENDPOINT = "/api/auth/user/";
    private static final String LOGIN_USER_ENDPOINT = "/api/auth/login/";
    private static final String EDIT_USER_ENDPOINT = "/api/auth/user/";
    private static final String AUTHORIZATION = "Authorization";

    public ValidatableResponse createUserClient(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(CREATE_USER_ENDPOINT)
                .then();
    }
    public void deleteUserClient(String accessToken) {
         given()
                .spec(getSpec())
                 .header("Authorization",accessToken)
                .delete(DELETE_USER_ENDPOINT)
                .then();
    }
    public ValidatableResponse loginUserClient(UserCreds userCreds) {
        return given()
                .spec(getSpec())
                .body(userCreds)
                .when()
                .post(LOGIN_USER_ENDPOINT)
                .then();
    }
    public ValidatableResponse editUserClientAuth(UserData userData, String accessToken) {
        return given()
                .spec(getSpec())
                .header(AUTHORIZATION,accessToken)
                .body(userData)
                .when()
                .patch(EDIT_USER_ENDPOINT)
                .then();
    }
    public ValidatableResponse editUserClientNotAuth(UserData userData) {
        return given()
                .spec(getSpec())
                .body(userData)
                .when()
                .patch(EDIT_USER_ENDPOINT)
                .then();
    }
}
