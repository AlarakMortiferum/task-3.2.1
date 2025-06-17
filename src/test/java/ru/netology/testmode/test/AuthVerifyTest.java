package ru.netology.testmode.test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataHelper;
import ru.netology.testmode.data.DataHelper.User;
import ru.netology.testmode.data.DbHelper;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class AuthVerifyTest {
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
    void shouldVerifyCodeAfterAuth() {
        User user = DataHelper.getRegisteredActiveUser();
        given().spec(spec).body(user)
                .when().post("/api/auth")
                .then().statusCode(200);

        String code = DbHelper.getLatestAuthCode(user.getLogin());
        assertNotNull(code);

        given().spec(spec)
                .body(new Object() { public final String login = user.getLogin(); public final String code = code; })
                .when().post("/api/auth/verify")
                .then().statusCode(200);
    }
}