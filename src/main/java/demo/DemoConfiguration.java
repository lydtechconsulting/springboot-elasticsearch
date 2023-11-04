package demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Slf4j
@EnableElasticsearchRepositories(basePackages = {"demo.repository"})
@ComponentScan(basePackages = {"demo"})
@Configuration
public class DemoConfiguration {
}
