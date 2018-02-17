package edu.iis.mto.blog.services;

import edu.iis.mto.blog.api.request.PostRequest;
import edu.iis.mto.blog.api.request.UserRequest;

public interface BlogService {

	Long createUser(UserRequest userRequest);

	Long createPost(Long userId, PostRequest postRequest);

	boolean addLikeToPost(Long userId, Long postId);

}
