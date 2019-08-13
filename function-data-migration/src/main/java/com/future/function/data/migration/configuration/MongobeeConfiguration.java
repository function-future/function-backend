package com.future.function.data.migration.configuration;

import com.github.mongobee.Mongobee;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongobeeConfiguration {

  private final String mongoUri;

  private final String dbName;

  public MongobeeConfiguration(MongoProperties mongoProperties) {

    this.mongoUri = mongoProperties.getUri();
    this.dbName = mongoProperties.getDatabase();
  }

  @Bean
  @ConditionalOnProperty(prefix = "migration",
                         name = "run")
  public Mongobee mongobee() {

    Mongobee runner = new Mongobee(mongoUri);

    runner.setDbName(dbName);
    runner.setChangeLogsScanPackage(
      "com.future.function.data.migration.change.log");
    runner.setEnabled(true);

    return runner;
  }

}
