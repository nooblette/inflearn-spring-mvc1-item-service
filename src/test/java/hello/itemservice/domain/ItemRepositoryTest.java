package hello.itemservice.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ItemRepositoryTest {

	ItemRepository itemRepository = new ItemRepository();

	@AfterEach
	void afterEach(){
		// 테스트를 하고나서 map에 저장된 내용을 모두 제거한다. (이전에 수행한 내용이 다음 테스트에 영향을 주지 않도록 하기 위함)
		itemRepository.clearStore();
	}
	@Test
	void save() {
		// given
		Item item = new Item("itemA", 10000, 10);

		// when
		Item savedItem = itemRepository.save(item);

		// then

		Item findItem = itemRepository.findById(savedItem.getId());
		assertThat(findItem).isEqualTo(savedItem);
	}

	@Test
	void findAll() {
		// given - List 조회 메서드는 2개 이상의 테스트 파라미터를 생성하여 확인해본다.
		Item itemA = new Item("itemA", 10000, 10);
		Item itemB = new Item("itemB", 20000, 20);
		itemRepository.save(itemA);
		itemRepository.save(itemB);

		// when
		List<Item> itemList = itemRepository.findAll();

		// then
		assertThat(itemList.size()).isEqualTo(2);
		assertThat(itemList).contains(itemA, itemB);
	}

	@Test
	void updateItem() {
		// given
		Item item = new Item("itemA", 10000, 10);
		Item savedItem = itemRepository.save(item);
		Long itemId = savedItem.getId();

		// when
		Item updateItem = new Item("itemB", 20000, 20);
		itemRepository.update(itemId, updateItem);

		// then
		Item findItem = itemRepository.findById(itemId);
		assertThat(findItem.getItemName()).isEqualTo(updateItem.getItemName());
		assertThat(findItem.getPrice()).isEqualTo(updateItem.getPrice());
		assertThat(findItem.getQuantity()).isEqualTo(updateItem.getQuantity());
	}

}