package kitchenpos.orderTable.repository;

import kitchenpos.orderTable.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
}
