package demo.util;

import demo.rest.api.CreateItemRequest;
import demo.rest.api.GetItemResponse;

public class TestRestData {

    public static CreateItemRequest buildCreateItemRequest(String name) {
        return CreateItemRequest.builder()
                .name(name)
                .build();
    }

    public static GetItemResponse buildGetItemResponse(String id, String name) {
        return GetItemResponse.builder()
                .id(id)
                .name(name)
                .build();
    }
}
