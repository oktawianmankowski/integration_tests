package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class SearchPostsUserTest extends FunctionalTests{

    @Before
    public void setUp() {
        JSONObject jsonPost = new JSONObject().put("entry", "post");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonPost.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user/1/post");
    }

    @Test
    public void searchingPostsShouldReturnLikesCountEqualsOne() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when().post("/blog/user/3/like/1");
        int likesCount = RestAssured.given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8").expect().log().all()
                .statusCode(HttpStatus.SC_OK).when().get("/blog/user/1/post").then().extract().jsonPath()
                .getInt("likesCount[0]");
        Assert.assertThat(likesCount, Matchers.is(1));
    }

    @Test
    public void searchingPostsShouldReturnLikesCountEqualsTwo() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when().post("/blog/user/3/like/2");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_OK).when().post("/blog/user/4/like/2");
        int likesCount = RestAssured.given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8").expect().log().all()
                .statusCode(HttpStatus.SC_OK).when().get("/blog/user/1/post").then().extract().jsonPath()
                .getInt("likesCount[1]");
        Assert.assertThat(likesCount, Matchers.is(2));
    }

    @Test
    public void searchingPostsByUserWithAccountStatusRemoved() {
        String likesCount = RestAssured.given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8").expect().log().all()
                .statusCode(HttpStatus.SC_OK).when().get("/blog/user/5/post").then().extract().asString();
        Assert.assertThat(likesCount, Matchers.is("[]"));
    }

}
