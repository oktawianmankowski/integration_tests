package edu.iis.mto.blog.domain.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class LikePost extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "postId", nullable = false)
	private BlogPost post;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BlogPost getPost() {
		return post;
	}

	public void setPost(BlogPost post) {
		this.post = post;
	}

}
