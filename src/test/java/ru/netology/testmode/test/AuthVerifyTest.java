package ru.netology.testmode.test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataHelper;
import ru.netology.testmode.data.User;
import ru.netology.testmode.data.DbHelper;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    static class AuthVerifyRequest {
        public final String login;
        public final String code;

        public AuthVerifyRequest(String login, String code) {
            this.login = login;
            this.code = code;
        }
    }

    @Test
    void shouldVerifyCodeAfterAuth() throws SQLException {
        User user = DataHelper.getRegisteredActiveUser();
        given().spec(spec).body(user)
                .when().post("/api/auth")
                .then().statusCode(200);

        String code = DbHelper.getLatestAuthCode(user.getLogin());
        assertNotNull(code);

        given().spec(spec)
                .body(new AuthVerifyRequest(user.getLogin(), code))
                .when().post("/api/auth/verify")
                .then().statusCode(200);
    }
}