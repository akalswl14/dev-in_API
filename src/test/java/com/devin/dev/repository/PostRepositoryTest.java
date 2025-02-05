package com.devin.dev.repository;

import com.devin.dev.dto.post.PostDto;
import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.user.User;
import com.devin.dev.entity.user.UserStatus;
import com.devin.dev.repository.post.PostRepository;
import com.devin.dev.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setDummyData() {
        User userA = new User("A", "a@b.com", "passA", "0001", UserStatus.ACTIVE);
        Post postA1 = new Post(userA, "PostA1", "ContentA1");
        Post postA2 = new Post(userA, "PostA2", "ContentA2");
        userA.getPosts().add(postA1);
        userA.getPosts().add(postA2);
        em.persist(postA1);
        em.persist(postA2);
        em.persist(userA);


        User userB = new User("B", "b@b.com", "passB", "0002", UserStatus.DELETED);
        Post postB1 = new Post(userB, "PostB1", "ContentB1");
        Post postB2 = new Post(userB, "PostB2", "ContentB2");
        Post postB3 = new Post(userB, "PostB3", "ContentB3");
        Post postB4 = new Post(userB, "PostB4", "ContentB4");
        userB.getPosts().add(postB1);
        userB.getPosts().add(postB2);
        userB.getPosts().add(postB3);
        userB.getPosts().add(postB4);
        em.persist(postB1);
        em.persist(postB2);
        em.persist(postB3);
        em.persist(postB4);
        em.persist(userB);


        User userC = new User("C", "c@b.com", "passC", "0003", UserStatus.DORMANT);
        Post postC1 = new Post(userC, "PostC1", "ContentC1");
        Post postC2 = new Post(userC, "PostC2", "ContentC2");
        Post postC3 = new Post(userC, "PostC3", "ContentC3");
        userC.getPosts().add(postC1);
        userC.getPosts().add(postC2);
        userC.getPosts().add(postC3);
        em.persist(postC1);
        em.persist(postC2);
        em.persist(userC);

        User userD = new User("D", "d@b.com", "passD", "0004", UserStatus.SUSPENDED);
        em.persist(userD);
    }

    @Test
    void findPostnamePageByUser() {

        User userA = userRepository.findByName("A").get();

        Pageable pageable = PageRequest.of(0, 4);
        Page<String> postNamesA = postRepository.findPostnamePageByUser(userA, pageable);

        assertThat(postNamesA.getTotalElements()).isEqualTo(2);
        assertThat(postNamesA).containsExactly("PostA1", "PostA2");

        User userD = userRepository.findByName("D").get();

        Page<String> postNamesD = postRepository.findPostnamePageByUser(userD, pageable);

        assertThat(postNamesD.getTotalElements()).isEqualTo(0);
    }

    @Test
    void findPostByUser() {

        User userA = userRepository.findByName("A").get();

        List<Post> postsA = postRepository.findPostByUser(userA);
        assertThat(postsA).extracting("title").containsExactly("PostA1", "PostA2");
        assertThat(postsA).extracting("content").containsExactly("ContentA1", "ContentA2");

        User userD = userRepository.findByName("D").get();
        List<Post> postsD = postRepository.findPostByUser(userD);

        assertThat(postsD.size()).isEqualTo(0);
    }

    @Test
    void findPostDtoByUser() {
        User userA = userRepository.findByName("A").get();

        List<PostDto> postDtosA = postRepository.findPostDtoByUser(userA);
        assertThat(postDtosA).extracting("title").containsExactly("PostA1", "PostA2");
        assertThat(postDtosA).extracting("content").containsExactly("ContentA1", "ContentA2");

        User userD = userRepository.findByName("D").get();
        List<PostDto> postDtosD = postRepository.findPostDtoByUser(userD);

        assertThat(postDtosD.size()).isEqualTo(0);
    }
}