# Spring Boot Demo With Elasticsearch

Spring Boot application demonstrating using Elasticsearch for reading and writing records.

Build Spring Boot application with Java 17:
```
mvn clean install
```

Start Docker containers:
```
docker-compose up -d
```

Start Spring Boot application:
```
java -jar target/springboot-elasticsearch-1.0.0.jar
```

In a terminal window use curl to submit a POST REST request to the application to create an item:
```
curl -i -X POST localhost:9001/v1/items -H "Content-Type: application/json" -d '{"name": "test-item"}'
```

A response should be returned with the 201 CREATED status code and the new item id in the Location header:
```
HTTP/1.1 201 
Location: KtBUmosB4cLHM8ui01Jf
```

The Spring Boot application should log the successful item persistence:
```
Item created with id: KtBUmosB4cLHM8ui01Jf
```

Get the item that has been created using curl:
```
curl -i -X GET localhost:9001/v1/items/KtBUmosB4cLHM8ui01Jf
```

A response should be returned with the 200 SUCCESS status code and the item in the response body:
```
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 28 Oct 2023 15:19:36 GMT

{"id":"KtBUmosB4cLHM8ui01Jf","name":"test-item"}
```

Stop containers:
```
docker-compose down
```

## Component Tests

The test demonstrates the application reading and writing records to Elasticsearch.

For more on the component tests see: https://github.com/lydtechconsulting/component-test-framework

Build Spring Boot application jar:
```
mvn clean install
```

Build Docker container:
```
docker build -t ct/springboot-elasticsearch:latest .
```

Run tests:
```
mvn test -Pcomponent
```

Run tests leaving containers up:
```
mvn test -Pcomponent -Dcontainers.stayup
```

Manual clean up (if left containers up):
```
docker rm -f $(docker ps -aq)
```

## Docker Clean Up

Manual clean up (if left containers up):
```
docker rm -f $(docker ps -aq)
```

Further docker clean up if network/other issues:
```
docker system prune
docker volume prune
```
