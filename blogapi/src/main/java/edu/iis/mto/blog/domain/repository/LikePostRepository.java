package edu.iis.mto.blog.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;

public interface LikePostRepository extends JpaRepository<LikePost, Long> {

	Optional<LikePost> findByUserAndPost(User user, BlogPost post);

}
