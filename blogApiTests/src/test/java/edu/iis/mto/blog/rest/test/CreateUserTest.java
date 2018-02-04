package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class CreateUserTest extends FunctionalTests {

	@Test
	public void postFormWithMalformedRequestDataReturnsBadRequest() {
		JSONObject jsonObj = new JSONObject().put("email", "tracy@domain.com");
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
				.post("/blog/user");
	}

	@Test
	public void addedUserShouldHaveUniqueEmail() {
		String notUniqueMail = "g@domain.com";

		JSONObject jsonObj = new JSONObject().put("email", notUniqueMail).put("firstName", "Grzegorz").put("lastName",
				"Komorowski");
		JSONObject jsonObjWithSameEmail = new JSONObject().put("email", notUniqueMail).put("firstName", "Dariusz")
				.put("lastName", "Komorowski");

		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
				.post("/blog/user");
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObjWithSameEmail.toString()).expect().log().all().statusCode(HttpStatus.SC_CONFLICT).when()
				.post("/blog/user");
	}
}
