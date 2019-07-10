package com.future.function.data.migration.configuration;

import com.github.mongobee.Mongobee;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongobeeConfiguration {
  
  @Value("${spring.data.mongodb.uri}")
  private String mongoUri;
  
  @Value("${spring.data.mongodb.database}")
  private String dbName;
  
  @Bean
  public Mongobee mongobee() {
    
    Mongobee runner = new Mongobee(mongoUri);
    
    runner.setDbName(dbName);
    runner.setChangeLogsScanPackage(
      "com.future.function.data.migration.change.log");
    runner.setEnabled(true);
    
    return runner;
  }
  
}
