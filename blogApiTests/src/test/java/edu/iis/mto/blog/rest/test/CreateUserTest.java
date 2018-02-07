package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.response.ResponseBody;
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
    public void shouldAddUser() {
        JSONObject jsonObj = new JSONObject().put("email", "tracy1@domain.com");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user");
    }

    @Test
    public void shouldAddPost() {
        JSONObject userObj = new JSONObject().put("email", "tracy3@domain.com");
        JSONObject postObj = new JSONObject().put("entry", "Post content");
        ResponseBody createUserResponse = RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(userObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user").body();

        Integer userId = createUserResponse.jsonPath().getJsonObject("id");

        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(postObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user/" + userId + "/post").body();

    }

    @Test
    public void shouldNotAllowToLikeOwnPost() {
        JSONObject userObj = new JSONObject().put("email", "tracy5@domain.com");
        JSONObject postObj = new JSONObject().put("entry", "Post content");
        ResponseBody createUserResponse = RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(userObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user").body();

        Integer userId = createUserResponse.jsonPath().getJsonObject("id");

        ResponseBody createPostResponse = RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(postObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user/" + userId + "/post").body();

        Integer postId = createPostResponse.jsonPath().getJsonObject("id");

        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(postObj.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
                .post("/blog/user/" + userId + "/like/" + postId);
    }


}
