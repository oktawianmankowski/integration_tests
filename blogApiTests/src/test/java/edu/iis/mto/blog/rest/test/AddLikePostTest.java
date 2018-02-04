package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class AddLikePostTest {

    @BeforeClass
    public static void setUp() {
        JSONObject jsonPost = new JSONObject().put("entry", "post");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonPost.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user/1/post");
    }

    @Test
    public void addingLikeToPostByUserWithAccountStatusToConfirmed() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when().post("/blog/user/3/like/1");
    }

    @Test
    public void notCanAddingLikeToOwnPostByUser() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when().post("/blog/user/1/like/1");
    }

    @Test
    public void notCanAddingLikeToPostByUserWithStatusToNew() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when().post("/blog/user/2/like/1");
    }

    @Test
    public void addingLikeToTheSamePostShouldNotChangeItsStatus() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when().post("/blog/user/3/like/1");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when().post("/blog/user/3/like/1");
        int likesCount = RestAssured.given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8").expect().log().all()
                .statusCode(HttpStatus.SC_OK).when().get("/blog/user/1/post").then().extract().jsonPath()
                .getInt("likesCount[0]");
        Assert.assertThat(likesCount, Matchers.is(1));
    }

}
