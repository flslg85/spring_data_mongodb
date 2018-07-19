package com.mongo.db.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    private RestTemplate restTemplate;

    @LocalServerPort
    private int port;


    @Before
    public void setUp() {
        restTemplate = new RestTemplate();
    }

    /**
     * Querydsl - DomainClassConverter test
     *
     *     @GetMapping("/{id}")
     *     public User findUser(@PathVariable("id") User user) {
     *         return user;
     *     }
     */
    @Test
    public void getDomainClassConverter() {
        HttpEntity<User> reqBody = new HttpEntity<>(new User().setName("ivory").setAge(35));
        String uriString = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/users").toUriString();
        User savedUser = restTemplate.exchange(uriString, HttpMethod.POST, reqBody, new ParameterizedTypeReference<User>() {}).getBody();

        User user = restTemplate.exchange(uriString + "/" + savedUser.getId(), HttpMethod.GET, null, new ParameterizedTypeReference<User>() {})
                .getBody();
        assertEquals("반환되는 user 는 ivory 이어야 한다.", "ivory", user.getName());
    }

    /**
     * ?name=bob => QUser.user.name.eq("bob")
     *
     * MongoTemplate
     *
     *     @GetMapping("/template")
     *     public List<User> findAllUsers(@NotNull User user, Pageable pageable) {
     *         Query query = new Query(
     *                 new Criteria().orOperator(
     *                         Criteria.where("name").is(user.getName()),
     *                         Criteria.where("age").is(user.getAge())))
     *                 .with(pageable);
     *         return template.find(query, User.class);
     *     }
     *
     * Querydsl - QuerydslPredicateArgumentResolver
     *
     *     @GetMapping
     *     public List<User> findAllUsers(@QuerydslPredicate(root = User.class) Predicate predicate, Pageable pageable) {
     *         return userRepository.findAll(predicate, pageable).getContent();
     *     }
     */
    @Test
    public void getUsersWithMongoTemplate() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/users" + "/template")
                .queryParam("name", "bob");

        List<User> users = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {})
                .getBody();

        assertEquals("반환되는 user 는 bob 이어야 한다.", "bob", users.get(0).getName());
    }

    @Test
    public void getUsersWithQuerydslPredicate() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/users" + "/querydsl")
                .queryParam("name", "bob");

        List<User> users = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {})
                .getBody();

        assertEquals("반환되는 user 는 bob 이어야 한다.", "bob", users.get(0).getName());
    }

    /**
     * ?page=0&size=2 => Pageable 에 자동 매핑됨.
     *
     * MongoTemplate
     *
     * Querydsl - QuerydslPredicateArgumentResolver
     *
     */
    @Test
    public void getUsersWithPageableMongoTemplate() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/users" + "/template")
                .queryParam("page", 0)
                .queryParam("size", 2)
                .queryParam("age", 10);

        List<User> users = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {})
                .getBody();

        Set<String> resNames = users.stream().map(User::getName).collect(toSet());

        assertEquals("반환되는 user 는 2명이어야 한다.", 2, users.size());
        assertEquals("반환되는 user 는 alice, bob 이어야 한다.", new HashSet<>(Arrays.asList("alice", "bob")), resNames);
    }

    @Test
    public void getUsersWithPageableQuerydsl() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/users" + "/querydsl")
                .queryParam("page", 0)
                .queryParam("size", 2)
                .queryParam("age", 10);

        List<User> users = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {})
                .getBody();

        Set<String> resNames = users.stream().map(User::getName).collect(toSet());

        assertEquals("반환되는 user 는 2명이어야 한다.", 2, users.size());
        assertEquals("반환되는 user 는 alice, bob 이어야 한다.", new HashSet<>(Arrays.asList("alice", "bob")), resNames);
    }
}