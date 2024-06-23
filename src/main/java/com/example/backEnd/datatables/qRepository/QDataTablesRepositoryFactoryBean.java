package com.example.backEnd.datatables.qRepository;

import jakarta.persistence.EntityManager;
import java.io.Serializable;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class QDataTablesRepositoryFactoryBean<R extends JpaRepository<T, ID>, T, ID extends Serializable>
        extends JpaRepositoryFactoryBean<R, T, ID> {

    public QDataTablesRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    protected @NotNull RepositoryFactorySupport createRepositoryFactory(@NotNull EntityManager entityManager) {
        return new DataTablesRepositoryFactory(entityManager);
    }

    private static class DataTablesRepositoryFactory extends JpaRepositoryFactory {
        DataTablesRepositoryFactory(EntityManager entityManager) {
            super(entityManager);
        }

        @Override
        protected @NotNull Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            Class<?> repositoryInterface = metadata.getRepositoryInterface();
            if (QDataTablesRepository.class.isAssignableFrom(repositoryInterface)) {
                return QDataTablesRepositoryImpl.class;
            } else {
                return super.getRepositoryBaseClass(metadata);
            }
        }
    }
}
