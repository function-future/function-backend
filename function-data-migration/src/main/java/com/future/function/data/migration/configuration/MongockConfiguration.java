package com.future.function.data.migration.configuration;

import com.github.cloudyrock.mongock.SpringBootMongock;
import com.github.cloudyrock.mongock.SpringBootMongockBuilder;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongockConfiguration {

  private final String mongoUri;

  private final String dbName;

  public MongockConfiguration(MongoProperties mongoProperties) {

    this.mongoUri = mongoProperties.getUri();
    this.dbName = mongoProperties.getDatabase();
  }

  @Bean
  @ConditionalOnProperty(prefix = "migration",
                         name = "run")
  public SpringBootMongock mongock(ApplicationContext springContext) {
    MongoClient client = new MongoClient(new MongoClientURI(mongoUri));
    return new SpringBootMongockBuilder(client, dbName, "com.future.function.data.migration.change.log")
        .setEnabled(true)
        .setLockQuickConfig()
        .setApplicationContext(springContext)
        .build();
  }

}
