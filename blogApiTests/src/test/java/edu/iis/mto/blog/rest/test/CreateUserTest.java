package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class CreateUserTest extends FunctionalTests {

    @Before
    public void init() {
        JSONObject jsonObj = new JSONObject().put("email", "tracy@domain.com");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().when().post("/blog/user");
    }

    @Test
    public void postFormWithMalformedRequestDataReturnsBadRequest() {
        JSONObject jsonObj = new JSONObject().put("email", "tracy@domain.com");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CONFLICT).when()
                .post("/blog/user");
    }

    @Test
    public void getFormWithMalformedRequestDataReturnsNotFound() {
        int id = 300;
        RestAssured.given().accept(ContentType.JSON).expect().log().all().statusCode(HttpStatus.SC_NOT_FOUND).when()
                .get("/blog/user/" + id);
    }

}
