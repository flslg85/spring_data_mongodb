package com.mongo.db.config;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

@Configuration
@EnableMongoRepositories(basePackages = {"com.mongo.db"})
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("{spring.data.mongodb.host}")
    private String host;

    @Value("{spring.data.mongodb.port}")
    private int port;

    @Value("{spring.data.mongodb.database}")
    private String database;

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(getServerAddresses(), getOptions());
    }

    private List<ServerAddress> getServerAddresses() {
        return Lists.newArrayList(new ServerAddress(host, port));
    }

    private MongoClientOptions getOptions() {
        return new MongoClientOptions.Builder().build();
    }

    @Override
    protected String getDatabaseName() {
        return database;
    }

//    /**
//     * Type Mapping 이 필요한 경우에 사용한다.
//     */
//    @Bean
//    @Override
//    public MappingMongoConverter mappingMongoConverter() throws Exception {
//        MappingMongoConverter mmc = super.mappingMongoConverter();
//        mmc.setTypeMapper(customTypeMapper());
//        return mmc;
//    }
//
//    @Bean
//    public MongoTypeMapper customTypeMapper() {
//        return new CustomMongoTypeMapper();
//    }

}
