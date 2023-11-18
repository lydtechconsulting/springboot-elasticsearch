package demo.component;

import java.util.List;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import demo.rest.api.CreateItemRequest;
import demo.util.TestRestData;
import dev.lydtech.component.framework.client.elastic.ElasticsearchCtfClient;
import dev.lydtech.component.framework.client.service.ServiceClient;
import dev.lydtech.component.framework.extension.ComponentTestExtension;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
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
@ExtendWith(ComponentTestExtension.class)
@ActiveProfiles("component-test")
public class EndToEndCT {

    @BeforeEach
    public void setup() {
        String serviceBaseUrl = ServiceClient.getInstance().getBaseUrl();
        RestAssured.baseURI = serviceBaseUrl;
    }

    /**
     * Use the REST API to create and retrieve an item that is persisted to Elasticsearch.
     */
    @Test
    public void testCreateAndRetrieveItem() throws Exception {
        String name = RandomStringUtils.randomAlphabetic(12);
        CreateItemRequest request = TestRestData.buildCreateItemRequest(name);
        Response createItemResponse = given()
                .header("Content-type", "application/json")
                .and()
                .body(request)
                .when()
                .post("/v1/items")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .response();
        String location = createItemResponse.header("Location");
        assertThat(location, notNullValue());
        log.info("Create item response location header: " + location);

        given()
                .pathParam("id", location)
                .when()
                .get("/v1/items/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .and()
                .body("name", containsString(name));
    }

    /**
     * Use the component-test-framework utility class to get the client to query Elasticsearch directly.
     */
    @Test
    public void testQueryElasticsearch() throws Exception {
        String name = RandomStringUtils.randomAlphabetic(12);
        CreateItemRequest request = TestRestData.buildCreateItemRequest(name);
        Response createItemResponse = given()
                .header("Content-type", "application/json")
                .and()
                .body(request)
                .when()
                .post("/v1/items")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .response();
        String location = createItemResponse.header("Location");
        assertThat(location, notNullValue());
        log.info("Create item response location header: " + location);

        assertThat(ElasticsearchCtfClient.getInstance().getBaseUrl(), notNullValue());

        // Search
        ElasticsearchClient esClient = ElasticsearchCtfClient.getInstance().getElasticsearchClient();
        SearchResponse<Item> searchResponse = esClient.search(s -> s
            .index("item")
            .query(q -> q
                .match(t -> t
                    .field("name")
                    .query(name))), Item.class);

        List<Hit<Item>> hits = searchResponse.hits().hits();
        assertThat(hits.size(), equalTo(1));
        assertThat(hits.get(0).source().getName(), equalTo(name));

        // Get individual
        GetResponse<Item> getResponse = esClient.get(s -> s
            .index("item")
            .id(location), Item.class);
        assertThat(getResponse.source().getName(), equalTo(name));
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class Item {
        private String name;
    }
}
