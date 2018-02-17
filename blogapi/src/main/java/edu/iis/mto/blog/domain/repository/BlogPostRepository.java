package edu.iis.mto.blog.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.User;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

	List<BlogPost> findByUser(User user);

}
