package com.example.backEnd.configurations;

import com.example.backEnd.datatables.qRepository.QDataTablesRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        repositoryFactoryBeanClass = QDataTablesRepositoryFactoryBean.class,
        basePackages = {"com.example.backEnd.repositories"})
public class QueryDslDatatablesConfig {}
