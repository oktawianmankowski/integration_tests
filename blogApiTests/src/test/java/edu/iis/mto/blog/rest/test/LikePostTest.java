package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class LikePostTest extends FunctionalTests {

    @Test
    public void confirmedUserCanNotLikeOwnLikePost() {
        JSONObject jsonPost = new JSONObject().put("entry", "test");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonPost.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user/1/post");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
                .log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when().post("/blog/user/1/like/1");
    }

    @Test
    public void confirmedUserCanLikeOthersPost() {
        JSONObject jsonPost = new JSONObject().put("entry", "test");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonPost.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user/1/post");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
                .log().all().statusCode(HttpStatus.SC_OK).when().post("/blog/user/2/like/1");
    }

}
