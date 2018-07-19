package com.mongo.db.user;

import com.mongo.db.person.Person;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.QBean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String>, QuerydslPredicateExecutor<User> {
    <T> Collection<T> findByName(String name, Class<T> type);
    List<Person> findByName(String name);
}
