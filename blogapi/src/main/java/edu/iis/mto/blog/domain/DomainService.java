package edu.iis.mto.blog.domain;

import org.springframework.beans.factory.annotation.Autowired;

import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;

public abstract class DomainService {

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected BlogPostRepository blogPostRepository;

	@Autowired
	protected LikePostRepository likePostRepository;

	@Autowired
	protected DataMapper mapper;
}
