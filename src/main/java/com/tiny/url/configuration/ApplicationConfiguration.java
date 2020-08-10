package com.tiny.url.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Configuration
@EnableSwagger2
public class ApplicationConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    public static void dropExisitingTables() {

        Connection connection = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:tinyurl.db");
            stmt = connection.createStatement();
            String sql = "DROP TABLE IF EXISTS url;";
            stmt.executeUpdate(sql);
            System.out.println( "Table dropped");

            stmt.close();
        }
        catch ( Exception e ) {
            System.out.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
}
