package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class LikePostTest extends FunctionalTests {

	@Test
	public void confirmedUserCanLikePost() {
		JSONObject jsonObject = new JSONObject().put("entry", "Entry");
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObject.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
				.post("/blog/user/3/like/1");
	}

	@Test
	public void unconfirmedUserCannotNotLikePost() {
		JSONObject jsonObject = new JSONObject().put("entry", "Entry");
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObject.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
				.post("/blog/user/2/like/1");
	}

	@Test
	public void userCannotLikeOwnPost() {
		JSONObject jsonObject = new JSONObject().put("entry", "Entry");
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObject.toString()).expect().log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when()
				.post("/blog/user/1/like/1");
	}
	
	@Test
	public void likingLikedPostDoesNotChangeStatus() {
		JSONObject jsonObject = new JSONObject().put("entry", "Entry");
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObject.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
				.post("/blog/user/3/like/1");
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
		.body(jsonObject.toString()).expect().log().all().statusCode(HttpStatus.SC_OK).when()
		.post("/blog/user/3/like/1");
	}
}
