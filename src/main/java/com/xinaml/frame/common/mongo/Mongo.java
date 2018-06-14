package com.xinaml.frame.common.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Mongo {
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void init (){
        mongoTemplate.findOne(new Query(),String.class);
    }
}
