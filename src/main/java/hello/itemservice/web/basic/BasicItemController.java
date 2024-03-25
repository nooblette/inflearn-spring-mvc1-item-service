package hello.itemservice.web.basic;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hello.itemservice.domain.Item;
import hello.itemservice.domain.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor // 생성자를 직접 선언하지 않고 final 키워드로 선언된 객촤와의 의존관계를 롬복이 자동으로 주입
public class BasicItemController {
	// @Autowired // Field Injection : 의존 관계를 주입받는 클래스의 필드에 직접 의존성을 주입
	// 간결하지만 테스트가 어렵고(테스트를 위해 스프링 컨테이너를 실행해야함) 객체 불변성을 해칠 수 있고 안정성을 보장하기 어렵다.
	// 순환 참조가 발생할 수 있다.(애플리케이션 복잡도 증가, 버그 유발 가능성 증가)
	// private ItemRepository itemRepository;

	private final ItemRepository itemRepository;
	// @Autowired // 생성자 주입 -> 생성자가 하나라면 @Autowired 애노테이션을 생략할 수 있다.
	// BasicItemController 클래스가 스프링 빈으로 등록되면서 생성자 주입으로 ItemRepository 클래스와의 의존 관계가 주입된다.
	// public BasicItemController(ItemRepository itemRepository){
	// 	this.itemRepository = itemRepository;
	// }

	@GetMapping
	public String items(Model model){
		List<Item> items = itemRepository.findAll();
		model.addAttribute("items", items); // key : items, value : List<Item> 형태로 모델(model)의 컬렉션에 담는다.
		return "basic/items";
	}

	// 테스트용 데이터 추가
	@PostConstruct // 의존성 주입이 이루어진 후 초기화를 수행 한다.(오직 한 번만 수행)
	/**
	 * 호출 순서
	 * 1. 생성자 호출
	 * 2. 의존성 주입 완료 (@Autowired or @RequiredArgsConstructor )
	 * 3. @PostConstruct 실행
	 * */
	public void init(){
		itemRepository.save(new Item("itemA", 10000, 10));
		itemRepository.save(new Item("itemB", 20000, 20));
	}
}
