package demo.service;

import java.util.Optional;

import demo.domain.Item;
import demo.exception.ItemNotFoundException;
import demo.repository.ItemRepository;
import demo.rest.api.CreateItemRequest;
import demo.rest.api.GetItemResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(@Autowired ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public String createItem(CreateItemRequest request) {
        Item item = Item.builder()
                .name(request.getName())
                .build();
        item = itemRepository.save(item);
        log.info("Item created with id: " + item.getId());
        return item.getId();
    }

    public GetItemResponse getItem(String itemId) {
        Optional<Item> itemOpt = itemRepository.findById(itemId);
        GetItemResponse getItemResponse;
        if(itemOpt.isPresent()) {
            log.info("Found item with id: " + itemOpt.get().getId());
            getItemResponse = GetItemResponse.builder()
                    .id(itemOpt.get().getId())
                    .name(itemOpt.get().getName())
                    .build();
        } else {
            log.warn("Item with id: " + itemId + " not found.");
            throw new ItemNotFoundException();
        }
        return getItemResponse;
    }
}
