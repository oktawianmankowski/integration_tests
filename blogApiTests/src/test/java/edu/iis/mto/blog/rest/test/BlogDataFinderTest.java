package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class BlogDataFinderTest extends FunctionalTests {

	@Test
	public void findingRemovedUserPostsShouldNotBePossible() {
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
				.log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when().get("/blog/user/3/post");
	}
}