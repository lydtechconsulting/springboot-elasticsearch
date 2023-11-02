package demo.component;

import dev.lydtech.component.framework.client.elastic.ElasticsearchClient;
import dev.lydtech.component.framework.client.service.ServiceClient;
import dev.lydtech.component.framework.extension.TestContainersSetupExtension;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
@ExtendWith(TestContainersSetupExtension.class)
@ActiveProfiles("component-test")
public class EndToEndCT {

    @BeforeEach
    public void setup() {
        String serviceBaseUrl = ServiceClient.getInstance().getBaseUrl();
        log.info("Service base URL is: {}", serviceBaseUrl);
        RestAssured.baseURI = serviceBaseUrl;
    }

    @Test
    public void testGetVersion() {
        given()
                .contentType("application/json")
                .when()
                .get("/v1/demo/version")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .and()
                .body("version", containsString("1.0"));
    }

    @Test
    public void testElasticSearchConnection() {
        ElasticsearchClient elasticsearchClient = ElasticsearchClient.getInstance();
        log.info("Elasticsearch container host: {} - port: {} - tcpPort: {}", elasticsearchClient.getHost(), elasticsearchClient.getPort(), elasticsearchClient.getTcpPort());
        assertThat(elasticsearchClient.getHost(), equalTo("localhost"));
        assertThat(elasticsearchClient.getPort(), notNullValue());
        assertThat(elasticsearchClient.getTcpPort(), notNullValue());
    }
}
