package edu.iis.mto.blog.mapper;

import edu.iis.mto.blog.api.request.PostRequest;
import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.dto.PostData;
import edu.iis.mto.blog.dto.UserData;

public interface DataMapper {

	User mapToEntity(UserRequest userRequest);

	BlogPost mapToEntity(PostRequest postRequest);

	UserData mapToDto(User user);

	PostData mapToDto(BlogPost blogPost);

}
