package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SearchUserTest extends FunctionalTests{

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
