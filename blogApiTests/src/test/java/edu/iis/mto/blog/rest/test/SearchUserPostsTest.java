package edu.iis.mto.blog.rest.test;

import static org.junit.Assert.assertEquals;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class SearchUserPostsTest extends FunctionalTests {

	@Before
	public void setUp() {
		JSONObject jsonPost = new JSONObject().put("entry", "post");	
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonPost.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
				.post("/blog/user/1/post");
	}

	@Test
	public void returnBadRequestWhenSearchingRemovedUsersPosts() {
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
				.log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when().get("/blog/user/4/post");
	}

	@Test
	public void searchingPostsShouldReturnCorrectLikesCount() {
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
				.log().all().statusCode(HttpStatus.SC_OK).when().post("/blog/user/3/like/1");
		int likesCount = RestAssured.given().accept(ContentType.JSON)
				.header("Content-Type", "application/json;charset=UTF-8").expect().log().all()
				.statusCode(HttpStatus.SC_OK).when().get("/blog/user/1/post").then().extract().jsonPath()
				.getInt("likesCount[0]");
		assertEquals(1, likesCount);
	}

}
