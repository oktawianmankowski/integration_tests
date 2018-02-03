package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class CreateUserTest extends FunctionalTests {

    @Test
    public void postFormWithMalformedRequestDataReturnsBadRequest() {
        JSONObject jsonObj = new JSONObject().put("email", "tracy@domain.com");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user");
    }

    @Test
    public void emailUserShouldBeUnique() {
        JSONObject jsonFirst = new JSONObject().put("email", "mail@domain.com");
        JSONObject jsonSecond = new JSONObject().put("email", "mail@domain.com");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonFirst.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonSecond.toString()).expect().log().all().statusCode(HttpStatus.SC_CONFLICT).when()
                .post("/blog/user");
    }

    @Test
    public void addingPostByUserWithAccountStatusToConfirmed() {
        JSONObject jsonObject = new JSONObject().put("entry", "post");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user/1/post");
    }

    @Test
    public void notCanAddingPostByUserWithAccountStatusToNew() {
        JSONObject jsonObject = new JSONObject().put("entry", "post");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
                .post("/blog/user/2/post");
    }

}
