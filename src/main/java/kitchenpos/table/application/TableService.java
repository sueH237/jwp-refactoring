package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.validator.TableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableValidator tableValidator;

    public TableService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository,
                        TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRepository.save(orderTableRequest.toOrderTable());
        return OrderTableResponse.of(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable OrderTable = findOrderTableById(orderTableId);
        validateJoinedTableGroup(orderTableId);
        tableValidator.validateOrderStatus(orderTableId);
        OrderTable.changeEmpty(orderTableRequest.getEmpty());
        OrderTable savedOrderTable = orderTableRepository.save(OrderTable);
        return OrderTableResponse.of(savedOrderTable);
    }

    private void validateJoinedTableGroup(Long orderTableId) {
        if (tableGroupRepository.existsByOrderTableId(orderTableId)) {
            throw new IllegalArgumentException("이미 그룹에 속한 테이블이 입니다.");
        }
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        OrderTable savedOrderTale = orderTableRepository.save(orderTable);
        return OrderTableResponse.of(savedOrderTale);
    }

}
