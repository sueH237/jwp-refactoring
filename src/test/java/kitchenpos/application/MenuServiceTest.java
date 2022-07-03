package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuServiceTest {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MenuProductRepository menuProductRepository;
    @Autowired
    MenuGroupRepository menuGroupRepository;
    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuService menuService;

    Product 스낵랩;
    Product 맥모닝;
    MenuProduct 스낵랩_메뉴_상품;
    MenuProduct 맥모닝_메뉴_상품;
    MenuGroup 패스트푸드류;

    @BeforeEach
    void setUp() {
        스낵랩 = new Product();
        스낵랩.setName("스낵랩");
        스낵랩.setPrice(BigDecimal.valueOf(3000));

        맥모닝 = new Product();
        맥모닝.setName("맥모닝");
        맥모닝.setPrice(BigDecimal.valueOf(4000));

        스낵랩_메뉴_상품 = new MenuProduct();
        스낵랩_메뉴_상품.setQuantity(1);
        스낵랩_메뉴_상품.setProduct(스낵랩);

        맥모닝_메뉴_상품 = new MenuProduct();
        맥모닝_메뉴_상품.setQuantity(1);
        맥모닝_메뉴_상품.setProduct(맥모닝);

        패스트푸드류 = new MenuGroup();
        패스트푸드류.setName("패스트푸드");
    }

    @AfterEach
    void tearDown() {
        menuProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
        menuGroupRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("미존재하는 메뉴 그룹으로 메뉴 등록 시 에러 반환")
    public void createNonExistsMenuGroup() {
        Menu menu = new Menu();
        menu.setMenuGroup(new MenuGroup());
        menu.setPrice(new Price(BigDecimal.valueOf(18000)));
        menu.setName("탕수육 세트");

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("미존재하는 상품으로 메뉴 구성시 에러 반황")
    public void createNonExistsProduct() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProduct(null);
        menuProduct.setQuantity(2);

        Menu menu = new Menu();
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);
        menu.setMenuProducts(new MenuProducts(menuProducts));
        menu.setPrice(new Price(BigDecimal.valueOf(18000)));

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 각 상품의 합보다 메뉴의 가격이 작지 않으면 등록 시 에러 반환")
    public void createNotCheaperPrice() {
        Menu menu = new Menu("모닝세트", BigDecimal.valueOf(8000), 패스트푸드류,
                new MenuProducts(Arrays.asList(스낵랩_메뉴_상품, 맥모닝_메뉴_상품)));
        menuGroupRepository.save(패스트푸드류);

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createSuccess() {
        menuGroupRepository.save(패스트푸드류);
        productRepository.save(스낵랩);

        Menu menu = new Menu("스낵랩 상품", BigDecimal.valueOf(3000), 패스트푸드류,
                new MenuProducts(Arrays.asList(스낵랩_메뉴_상품)));
        assertThat(menuService.create(menu).getId()).isNotNull();
    }

    @Test
    public void list() {
        menuGroupRepository.save(패스트푸드류);
        스낵랩_메뉴_상품.setProduct(productRepository.save(스낵랩));
        맥모닝_메뉴_상품.setProduct(productRepository.save(맥모닝));

        MenuProduct 모닝_스낵랩 = new MenuProduct();
        모닝_스낵랩.setQuantity(1);
        모닝_스낵랩.setProduct(스낵랩);

        Menu 스낵랩_세트 = new Menu("스낵랩 상품", BigDecimal.valueOf(3000), 패스트푸드류,
                new MenuProducts(Arrays.asList(스낵랩_메뉴_상품)));
        Menu 모닝_세트 = new Menu( "모닝_세트", BigDecimal.valueOf(7000), 패스트푸드류,
                new MenuProducts(Arrays.asList( 모닝_스낵랩, 맥모닝_메뉴_상품)));

        menuRepository.save(스낵랩_세트);
        menuRepository.save(모닝_세트);

        List<Menu> list = menuService.list();
        assertThat(list).hasSize(2);
    }
}