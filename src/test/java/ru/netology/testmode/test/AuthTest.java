package ru.netology.testmode.test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataHelper.User;
import ru.netology.testmode.data.DataHelper;

import static io.restassured.RestAssured.given;

public class AuthTest {
    private static RequestSpecification spec;

    @BeforeAll
    static void setup() {
        spec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(9999)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    @Test
    void shouldLoginWithActive() {
        User user = DataHelper.getRegisteredActiveUser();
        given().spec(spec).body(user)
                .when().post("/api/auth")
                .then().statusCode(200);
    }

    @Test
    void shouldNotLoginWithBlocked() {
        User user = DataHelper.getRegisteredBlockedUser();
        given().spec(spec).body(user)
                .when().post("/api/auth")
                .then().statusCode(403);
    }

    @Test
    void shouldNotLoginWrongLogin() {
        User user = DataHelper.getWrongLoginUser();
        given().spec(spec).body(user)
                .when().post("/api/auth")
                .then().statusCode(401);
    }

    @Test
    void shouldNotLoginWrongPassword() {
        User user = DataHelper.getWrongPasswordUser();
        given().spec(spec).body(user)
                .when().post("/api/auth")
                .then().statusCode(401);
    }
}