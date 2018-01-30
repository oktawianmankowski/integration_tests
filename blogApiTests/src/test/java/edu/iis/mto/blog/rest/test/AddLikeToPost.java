package edu.iis.mto.blog.rest.test;


import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

public class AddLikeToPost extends FunctionalTests {
    @Test
    public void postAuthorShouldNotLikeOwnPost() {
        JSONObject jsonObj = getJsonObject("Post");
        RESTAssurated(jsonObj, "/blog/user/1/like/1", HttpStatus.SC_BAD_REQUEST);
    }

    private void RESTAssurated(JSONObject jsonObj, String s, int scBadRequest) {
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString()).expect().log().all().statusCode(scBadRequest).when()
                .post(s);
    }

    private JSONObject getJsonObject(String post) {
        return new JSONObject().put("entry", post);
    }
}
