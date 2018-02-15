package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ResponseBody;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class SearchPostTest extends FunctionalTests{

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

}
