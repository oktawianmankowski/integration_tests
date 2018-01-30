package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

public class AddPostTest extends FunctionalTests {
    @Test
    public void onlyUserWithCONFIRMEDStatusOfAccountCanAddPost() {

        JSONObject first = getJsonObject("Post numer 1");

        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(first.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user/1/post");

        JSONObject second = getJsonObject("Post numer 2");

        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(second.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
                .post("/blog/user/2/post");

    }

    private JSONObject getJsonObject(String value) {
        return new JSONObject().put("entry", value);
    }
}
