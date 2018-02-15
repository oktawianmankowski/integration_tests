package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ResponseBody;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

public class AddLikeTest extends FunctionalTests {

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

}
