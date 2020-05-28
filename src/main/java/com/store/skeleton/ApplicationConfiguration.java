package com.store.skeleton;

import com.store.services.webapi.datastore.Repository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ApplicationConfiguration {

  @Bean("accountService")
  @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
  public Repository getRepository() {
    return new Repository();
  }


}
