package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class SearchUsersTest extends FunctionalTests{

    @Test
    public void searchingUserByIdWithAccountStatusRemovedShouldNotFound() {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .expect().log().all().statusCode(HttpStatus.SC_NOT_FOUND).when().get("/blog/user/5");
    }

    @Test
    public void searchingUsersByStringWithOnlyAccountStatusRemovedShouldBeEmpty() {
        String userCount = RestAssured.given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8").expect().log().all()
                .statusCode(HttpStatus.SC_OK).when().get("/blog/user/find?searchString=Delete")
                .then().extract().asString();
        Assert.assertThat(userCount, Matchers.is("[]"));
    }

    @Test
    public void searchingUsersByStringShouldReturnWithoutUserWithAccountStatusRemoved() {
        int userCount = RestAssured.given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8").expect().log().all()
                .statusCode(HttpStatus.SC_OK).when().get("/blog/user/find?searchString=John").then().extract()
                .jsonPath().getList("$").size();
        Assert.assertThat(userCount, Matchers.is(1));
    }

}
