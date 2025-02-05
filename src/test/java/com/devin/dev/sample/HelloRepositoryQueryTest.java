package com.devin.dev.sample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class HelloRepositoryQueryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    HelloRepository helloRepository;

    @BeforeEach
    public void initData() {
        Hello a = new Hello("a");
        Hello b = new Hello("b");
        Hello c = new Hello("c");
        Hello d = new Hello("d");

        em.persist(a);
        em.persist(b);
        em.persist(c);
        em.persist(d);
    }

    @Test
    public void findAll() {
        List<Hello> allHello = helloRepository.findAllCustom();
        assertThat(allHello)
                .extracting("data")
                .containsExactly("a", "b", "c", "d");
    }

    @Test
    public void findByName() {
        Hello e = new Hello("e");
        em.persist(e);

        Hello findHello = helloRepository.findByIdCustom(e.getId()).get();
        assertThat(findHello).isEqualTo(e);
    }

    @Test
    public void count() {
        long count = helloRepository.countCustom();

        assertThat(count).isEqualTo(4L);
    }

}