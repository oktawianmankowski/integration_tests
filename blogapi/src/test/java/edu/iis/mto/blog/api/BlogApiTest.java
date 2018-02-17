package edu.iis.mto.blog.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityNotFoundException;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.dto.Id;
import edu.iis.mto.blog.services.BlogService;
import edu.iis.mto.blog.services.DataFinder;

@RunWith(SpringRunner.class)
@WebMvcTest(BlogApi.class)
public class BlogApiTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private BlogService blogService;

	@MockBean
	private DataFinder finder;

	private UserRequest user;

	@Before
	public void setUp() {
		user = new UserRequest();
		user.setEmail("john@domain.com");
		user.setFirstName("John");
		user.setLastName("Steward");
	}

	@Test
	public void postBlogUserShouldResponseWithStatusCreatedAndNewUserId() throws Exception {
		Long newUserId = 1L;
		Mockito.when(blogService.createUser(user)).thenReturn(newUserId);
		String content = writeJson(user);

		mvc.perform(post("/blog/user").contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8).content(content)).andExpect(status().isCreated())
				.andExpect(content().string(writeJson(new Id(newUserId))));
	}

	@Test
	public void dataIntegrityViolationExceptionShouldReturn409Status() throws Exception {
		doThrow(DataIntegrityViolationException.class).when(blogService).createUser(user);
		String content = writeJson(user);

		mvc.perform(post("/blog/user").contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8).content(content)).andExpect(status().isConflict());
	}

	@Test
	public void notExistingUserShouldReturn404Status() throws Exception {
		Long userId = 1L;
		doThrow(EntityNotFoundException.class).when(finder).getUserData(userId);
		mvc.perform(get("/blog/user/{id}", userId)).andExpect(status().isNotFound());
	}

	private String writeJson(Object obj) throws JsonProcessingException {
		return new ObjectMapper().writer().writeValueAsString(obj);
	}

}
