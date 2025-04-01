package com.example.Crud;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomUserRepository {
    private final MongoTemplate mongoTemplate;
    public List<User> searchByNameOrEmail(String keyword){
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("name").regex(keyword,"i"),
                Criteria.where("email").regex(keyword,"i")
        ));
        return mongoTemplate.find(query,User.class);
    }
}
