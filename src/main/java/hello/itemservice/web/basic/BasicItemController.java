package hello.itemservice.web.basic;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

	@GetMapping("/{itemId}")
	public String item(@PathVariable("itemId") long itemId, Model model){
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);

		return "basic/item";
	}

	// 실제로 상품을 등록하는게 아니라, 등록할 수 있는 폼(Form)을 보여만 준다.
	@GetMapping("/add")
	public String addForm(){
		return "basic/addForm";
	}

	// 같은 URL(/basic/items/add)에 대해 HTTP Method로 기능을 구분한다.
	//@PostMapping("/add")
	// 요청 파라미터의 값은 입력 폼의 input 태그 중 name의 값을 기준으로 넘어온다.
	// e.g. 다음 태그에서는 name의 값인 itemName을 기준으로 요청 값이 넘어온다.
	// - <input type="text" id="itemName" name="itemName" class="form-control" value="상품A" th:value="${item.itemName}" readonly>
	public String addItemV1(@RequestParam("itemName") String itemName,
					   @RequestParam("price") Integer price,
					   @RequestParam("quantity") Integer quantity,
					   Model model){

		Item item = new Item();
		item.setItemName(itemName);
		item.setPrice(price);
		item.setQuantity(quantity);

		// 상품 저장
		itemRepository.save(item);

		// 상품을 저장하고 나면, 상품 상세 정보 뷰를 재사용하여 그 결과를 보여준다.
		model.addAttribute("item", item);
		return "basic/item";
	}

	//@PostMapping("/add")
	public String addItemV2(@ModelAttribute("item") Item item, Model model){
		// ModelAttribute는 아래 코드(Item 객체를 생성하고 값을 세팅)를 그대로 대신 수행해준다.
		// Item item = new Item();
		// item.setItemName(itemName);
		// item.setPrice(price);
		// item.setQuantity(quantity);

		itemRepository.save(item);

		// ModelAttribute는 model 객체에 값을 넣어주는 로직(addAttribute() 메서드 호출)도 대신 수행해준다.
		// 이때, 컬렉션의 key 값은 @ModelAttribute 선언시 지정한 값(이 경우에선 "item")으로 지정된다.
		// model.addAttribute("item", item);
		return "basic/item";
	}

	@PostMapping("/add")
	public String addItemV3(@ModelAttribute Item item, Model model){
		// ModelAttribute는 아래 코드(Item 객체를 생성하고 값을 세팅)를 그대로 대신 수행해준다.
		// Item item = new Item();
		// item.setItemName(itemName);
		// item.setPrice(price);
		// item.setQuantity(quantity);

		itemRepository.save(item);

		// ModelAttribute는 model 객체에 값을 넣어주는 로직(addAttribute() 메서드 호출)도 대신 수행해준다.
		// 이때, 컬렉션의 key 값은 @ModelAttribute 선언시 지정한 값(이 경우에선 "item")으로 지정된다.
		// model.addAttribute("item", item);
		return "basic/item";
	}

	// 상품 수정 폼을 보여주기만 한다.
	@GetMapping("/{itemId}/edit")
	public String editForm(@PathVariable("itemId") Long itemId, Model model){
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);

		return "basic/editForm";
	}

	// 같은 URL(/{itemId}/edit)에 대해 HTTP Method로 기능을 구분한다.
	@PostMapping("/{itemId}/edit")
	public String edit(@PathVariable("itemId") Long itemId, @ModelAttribute Item item){
		itemRepository.update(itemId, item);

		// 저장 성공 후에는 리다이렉트(redirect)로 이동
		return "redirect:/basic/items/{itemId}";
	}
}
