package hello.itemservice.domain;

import lombok.Data;

@Data // Getter, Setter, RequiredArgsConstructor, ToString, EqualsAndHashCode, Value를 자동으로 생성한다
// 핵심 도메인에서 @Data와 같이 모든 기능을 제공하는 애노테이션은 예상치 못한 동작을 유발하여 사용에 주의한다.
// 실무에서는 Getter, Setter를 기본으로 사용하고 이 외에 필요한 기능들만 따로 애노테이션을 선언하여 사용하는 것을 권고
public class Item {
	private Long id;
	private String itemName;
	private Integer price; // price가 null인 경우를 핸들링하기 위해 Wrapper Class(Integer)로 선언
	private Integer quantity;

	public Item() {
	}

	public Item(String itemName, Integer price, Integer quantity) {
		this.itemName = itemName;
		this.price = price;
		this.quantity = quantity;
	}
}
