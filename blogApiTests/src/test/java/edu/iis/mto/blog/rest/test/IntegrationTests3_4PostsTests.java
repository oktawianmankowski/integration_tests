package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class IntegrationTests3_4PostsTests extends FunctionalTests {

    @Test
    public void userWithoutCONFIRMEDStatusTryToPostTest() {

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("entry", "Post");

        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
                .post("/blog/user/2/post");

    }

}
