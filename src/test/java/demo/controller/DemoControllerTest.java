package demo.controller;

import demo.rest.api.VersionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class DemoControllerTest {

    private DemoController controller;

    @BeforeEach
    public void setUp() {
        controller = new DemoController();
    }

    @Test
    public void testVersion() {
        ResponseEntity<VersionResponse> response = controller.version();
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody().getVersion(), equalTo("1.0"));
    }
}
