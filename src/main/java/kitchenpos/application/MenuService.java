package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Price;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository) {
        this.menuRepository = menuRepository;
    }

    @Transactional
    public Menu create(final Menu menu) {
        final Price price = menu.getPrice();

        validateMenuGroup(menu);

        final MenuProducts menuProducts = menu.getMenuProducts();
        menuProducts.validateTotalPriceNotExpensiveThanEach(price);

        return menuRepository.save(menu);
    }

    private void validateMenuGroup(Menu menu) {
        if (menu.getMenuGroup() != null && menu.getMenuGroup().getId() == null) {
            throw new IllegalArgumentException();
        }
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
