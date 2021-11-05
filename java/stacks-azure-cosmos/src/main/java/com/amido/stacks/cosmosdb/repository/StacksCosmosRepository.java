package com.amido.stacks.cosmosdb.repository;

import com.amido.stacks.core.repository.StacksPersistence;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface StacksCosmosRepository<T>
    extends StacksPersistence<T>, CosmosRepository<T, String> {}
