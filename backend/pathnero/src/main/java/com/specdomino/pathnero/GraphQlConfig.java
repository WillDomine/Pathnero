package com.specdomino.pathnero;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.GraphQlSource;

import graphql.schema.GraphQLSchema;

import org.springframework.core.io.ClassPathResource;

@Configuration
public class GraphQlConfig {

    @Bean
    public GraphQlSource graphQlSource() {
        // Building the GraphQL source from the schema resource and adding resolvers
        return GraphQlSource.schemaResourceBuilder()
            .schemaResources(new ClassPathResource("graphql/schema.graphqls"))  // Make sure your schema file is in the correct location
            .build();
    }

}

