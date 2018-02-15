package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ResponseBody;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

public class FunctionalTests {

    @BeforeClass
    public static void setup() {
        String port = System.getProperty("server.port");
        if (port == null) {
            RestAssured.port = Integer.valueOf(8080);
        } else {
            RestAssured.port = Integer.valueOf(port);
        }

        String basePath = System.getProperty("server.base");
        if (basePath == null) {
            basePath = "";
        }
        RestAssured.basePath = basePath;

        String baseHost = System.getProperty("server.host");
        if (baseHost == null) {
            baseHost = "http://localhost";
        }
        RestAssured.baseURI = baseHost;

    }

    @Test
    public void addUserWithUniqueMailShouldReturnCreateStatus() {
        JSONObject jsonObj = new JSONObject().put("email", "adam@test.pl");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user");
    }

    @Test
    public void addUserWithoutUniqueMailShouldReturnConflictStatus() {
        JSONObject jsonObj = new JSONObject().put("email", "john@domain.com");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CONFLICT).when()
                .post("/blog/user");
    }

    @Test
    public void addPostByConfirmedUserShouldReturnCreateStatus() {
        JSONObject jsonObj = new JSONObject().put("entry", "Post");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
                .post("/blog/user/" + 1 + "/post");
    }

    @Test
    public void addPostByNewUserShouldReturnBadRequestStatus() {
        JSONObject jsonObj = new JSONObject().put("entry", "Post");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
                .post("/blog/user/" + 3 + "/post");
    }

    @Test
    public void addLikeByConfirmedUserShouldReturnCreateStatus() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
                .log().all().statusCode(HttpStatus.SC_OK).when().post("/blog/user/" + 2 + "/like/" + 1);
    }

    @Test
    public void addLikeByConfirmedUserToOwnShouldReturnBadRequestStatus() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
                .log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when().post("/blog/user/" + 1 + "/like/" + 1);
    }

    @Test
    public void addLikeByNewUserShouldReturnBadRequestStatus() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
                .log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when().post("/blog/user/" + 1 + "/like/" + 1);
    }

    @Test
    public void addLikeTwiceBySameUserToSamePostShouldNotChangeLikeState() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
                .log().all().statusCode(HttpStatus.SC_OK).when().post("/blog/user/" + 2 + "/like/" + 1);

        ResponseBody responseBody = RestAssured.given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8").expect().log().all()
                .statusCode(HttpStatus.SC_OK).when().post("/blog/user/" + 2 + "/like/" + 1).getBody();

        String response = responseBody.print();

        Assert.assertFalse(Boolean.valueOf(response));
    }

    @Test
    public void searchPostOfDeleteUserShouldReturnNotFoundResponse() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
                .log().all().statusCode(HttpStatus.SC_NOT_FOUND).when().get("/blog/user/" + 5 + "/post");

    }

    @Test
    public void searchPostOfUserShouldReturnCorrectLikeCountForPost() {
        int expectedLikescount = 1;
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
                .log().all().statusCode(HttpStatus.SC_OK).when().post("/blog/user/" + 2 + "/like/" + 3);

        ResponseBody responseBody = RestAssured.given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8").expect().log().all()
                .statusCode(HttpStatus.SC_OK).when().get("/blog/user/" + 4 + "/post").getBody();

        JSONArray jsonArray = new JSONArray(responseBody.print());
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        int likesCount = (int) jsonObject.get("likesCount");

        Assert.assertThat(likesCount, is(expectedLikescount));
    }

    @Test
    public void searchActiveUserByStringSearchShouldReturnOkResponse() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("searchString", "John");

        RestAssured.given().parameters(map).accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8").expect().log().all()
                .statusCode(HttpStatus.SC_OK).when().get("/blog/user/find");
    }

    @Test
    public void searchUserByStringSearchShouldReturnOnlyNotRemovedUser() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("searchString", "Will");

        RestAssured.given().parameters(map).accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8").expect().log().all()
                .statusCode(HttpStatus.SC_OK).when().get("/blog/user/find");
    }

    @Test
    public void searchRemovedUserByIDSearchShouldReturnNoFoundResponse() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
                .log().all().statusCode(HttpStatus.SC_NOT_FOUND).when().get("/blog/user/" + 5);
    }

}
