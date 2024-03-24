package hello.itemservice.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository // Component Scan 대상에 포함 (ItemRepository를 빈으로 등록한다)
public class ItemRepository {


	// 실무에서는 Map을 그대로 사용하지 않음, 멀티쓰레드 환경에서 여러 곳에 동시에 store Map에 접근할때 동시성을 보장하지 못한다.
	// 멀티쓰레드 환경에서는 ConcurrentHashMap을 사용한다.
	private static final Map<Long, Item> store = new HashMap<>(); // static으로 선언한 것에 주의

	// 멀티쓰레드 환경에서는 AtomicLong 등을 사용한다. (long은 동시성을 보하지 못함)
	private static long sequence = 0L; // static으로 선언한 것에 주의

	public Item save(Item item){
		item.setId(++sequence);
		store.put(item.getId(), item);
		return item;
	}

	public Item findById(Long id){
		return store.get(id);
	}

	public List<Item> findAll(){
		// findAll() 메서드가 반환해주는 값을 안전하게 사용하기 위해 values() 메서드로 값을 조회한 뒤 ArrayList<>로 감싸준다.
		return new ArrayList<>(store.values());
	}

	public void update(Long ItemId, Item updateParam){
		Item findItem = findById(ItemId);

		// 이 경우 ItemId 값은 사용되지 않기 때문에 모든 필드 값을 setter로 꺼내는 것이 아니라 ItemName, Price, Quantity 필드만 갖는 업데이트용 객체를 새로 생성하는게 더 적절하다.
		// - ItemName, Price, Quantity 필드만 갖는 업데이트용 객체를 새로 선언하는게 코드 중복을 발생시킨다는 점이 고민이 될 수 있다.
		// - 하지만 이와 같은 코드에서는 다른 개발자가 봤을때 ItemId는 왜 사용하지 않지? 와 같은 의문을 가질 수 있다. (불명확 코드)
		// - 중복과 명확성 사이에서 고민이 될 때는 명확성을 따르자
		findItem.setItemName(updateParam.getItemName());
		findItem.setPrice(updateParam.getPrice());
		findItem.setQuantity(updateParam.getQuantity());
	}

	public void clearStore(){
		store.clear();
	}
}
