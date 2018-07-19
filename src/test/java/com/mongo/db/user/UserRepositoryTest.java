package com.mongo.db.user;

import com.mongo.db.person.Person;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testQuerydsl() {
        Predicate predicate = QUser.user.name.eq("bob").or(QUser.user.name.eq("diana"));
        List<User> users = (List<User>) userRepository.findAll(predicate);
        assertEquals("검색된 user 는 2명이어야 한다.", 2, users.size());
        assertEquals("검색된 user 는 bob 과 diana 이어야 한다.",
                new HashSet<>(Arrays.asList("bob", "diana")), users.stream().map(User::getName).collect(toSet()));
    }

    @Test
    public void testDynamicProjections1() {
        Collection<Person> people = userRepository.findByName("bob", Person.class);
        assertEquals("검색된 people 는 1명이어야 한다.", 1, people.size());
        assertEquals("검색된 people 는 bob 이어야 한다.",
                new HashSet<>(Collections.singletonList("bob")), people.stream().map(Person::getName).collect(toSet()));
    }
    @Test
    public void testDynamicProjections2() {
        Collection<Person> people = userRepository.findByName("bob");
        assertEquals("검색된 people 는 1명이어야 한다.", 1, people.size());
        assertEquals("검색된 people 는 bob 이어야 한다.",
                new HashSet<>(Collections.singletonList("bob")), people.stream().map(Person::getName).collect(toSet()));
    }
}