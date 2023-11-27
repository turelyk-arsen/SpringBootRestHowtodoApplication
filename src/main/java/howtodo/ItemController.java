package howtodo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class ItemController {
	
	@Autowired
	ItemRepository itemRepository;
	
	@GetMapping("/items")
	public List<Item> all() {
		return itemRepository.findAll();
	}
	
	@GetMapping("/items/{id}")
	public Item getById(@PathVariable Long id) {
		return itemRepository.findById(id)
				.orElseThrow(()-> new ItemNotFoundException(id));
	}
	
	@PostMapping("/items")
	public Item creatItem(@Valid @RequestBody Item item) {
		return itemRepository.save(item);
	}
	
	@DeleteMapping("/items/{id}")
	public void deleteItem(@PathVariable Long id) {
		itemRepository.deleteById(id);
	}
	
	@PutMapping("/item/{id}")
	public Item updateOrCreate(@PathVariable Long id, @RequestBody Item newItem) {
		return itemRepository.findById(id)
				.map(item -> {
					item.setName(newItem.getName());
					return itemRepository.save(item);
				})
				.orElseGet(()-> {
					newItem.setId(id);
					return itemRepository.save(newItem);
				});
	}
}
