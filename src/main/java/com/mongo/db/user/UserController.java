package com.mongo.db.user;

import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final MongoTemplate template;

    @Autowired
    public UserController(UserRepository userRepository, MongoTemplate template) {
        this.userRepository = userRepository;
        this.template = template;
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable("id") User user) {
        return user;
    }

    @PostMapping
    public User saveUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/querydsl")
    public List<User> findAllUsers(@QuerydslPredicate(root = User.class) Predicate predicate, Pageable pageable) {
        return userRepository.findAll(predicate, pageable).getContent();
    }

    @GetMapping("/template")
    public List<User> findAllUsers(@NotNull User user, Pageable pageable) {
        Query query = new Query(
                new Criteria().orOperator(
                        Criteria.where("name").is(user.getName()),
                        Criteria.where("age").is(user.getAge())))
                .with(pageable);
        return template.find(query, User.class);
    }
}
