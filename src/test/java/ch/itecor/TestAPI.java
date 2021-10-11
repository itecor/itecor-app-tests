package ch.itecor;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class TestAPI {
    @Test
    public void technicalTestAPI(){
        // API
        // 1. Get the json webtoken using a POST request with
        RestAssured.baseURI = "https://itecor-app.herokuapp.com/";
//        RestAssured.baseURI = "http://localhost:3000";
        String jsonBody = "{" +
                "\"username\": \"User1\"," +
                "\"password\": \"Password1234\"" +
        "}";
        RequestSpecification httpRequest = RestAssured.given().header("Content-Type", "application/json");
        Response response = httpRequest.body(jsonBody).request(Method.POST, "/api/login");
        // Récupération du cookie ssoTokenVal
        String token = response.getBody().asString();

        // 2. Reuse the webtoken found in 1 to find the number of consultant seats available on Geneva office
        jsonBody = "{" +
                "\"office\": \"Geneva\"," +
                "\"token\": " + token +
                "}";
        String result = httpRequest.body(jsonBody).request(Method.POST, "/api/count-seats").asString();
        Assertions.assertThat(result).contains("The Geneva office has 10 bookable seats!");
    }
}
