package edu.iis.mto.blog.rest.test;

import static org.junit.Assert.assertEquals;

import org.apache.http.HttpStatus;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class SearchUsersTest extends FunctionalTests {

	@Test
	public void userByIdWithStatusRemovedShouldBeNotFound() {
		int userCount = RestAssured.given().accept(ContentType.JSON)
				.header("Content-Type", "application/json;charset=UTF-8").expect().log().all()
				.statusCode(HttpStatus.SC_OK).when().get("/blog/user/find?searchString=Kowalski").then().extract()
				.jsonPath().getList("$").size();
		assertEquals(1, userCount);
	}

}
