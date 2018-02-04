package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.dto.PostData;
import edu.iis.mto.blog.dto.UserData;
import edu.iis.mto.blog.services.DataFinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Service
public class BlogDataFinder extends DomainService implements DataFinder {

    @Override
    public UserData getUserData(Long userId) {
        User user = userRepository.findOne(userId);
        if (user == null) {
            throw new EntityNotFoundException(String.format("user with id %1 does not exists", userId));
        }
        if (user.getAccountStatus() == AccountStatus.REMOVED) {
            throw new EntityNotFoundException("user is removed");
        }
        return mapper.mapToDto(user);
    }

    @Override
    public List<UserData> findUsers(String searchString) {
        List<User> users = userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                searchString, searchString, searchString);
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getAccountStatus() == AccountStatus.REMOVED) {
                users.remove(i);
            }
        }
        return users.stream().map(user -> mapper.mapToDto(user)).collect(Collectors.toList());
    }

    @Override
    public PostData getPost(Long userId) {
        BlogPost blogPost = blogPostRepository.findOne(userId);
        return mapper.mapToDto(blogPost);
    }

    @Override
    public List<PostData> getUserPosts(Long userId) {
        User user = userRepository.findOne(userId);
        if (user.getAccountStatus() == AccountStatus.REMOVED) {
            throw new EntityNotFoundException("user is removed");
        }
        List<BlogPost> posts = blogPostRepository.findByUser(user);
        return posts.stream().map(post -> mapper.mapToDto(post)).collect(Collectors.toList());
    }

}
