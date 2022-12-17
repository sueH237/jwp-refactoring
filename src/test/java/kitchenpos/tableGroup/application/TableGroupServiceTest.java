package kitchenpos.tableGroup.application;

import kitchenpos.order.repository.OrderRepository;
import kitchenpos.orderTable.domain.OrderTable;
import kitchenpos.orderTable.repository.OrderTableRepository;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup 단체1;
    private OrderTable 테이블1;
    private OrderTable 테이블2;
    private OrderTable 테이블3;
    private OrderTable 테이블_GUEST3_NOT_EMPTY;
    private OrderTable 테이블_TABLEGROUP;

    @BeforeEach
    void setUp() {
        테이블1 = new OrderTable(1L, 0, true);
        테이블2 = new OrderTable(2L, 0, true);
        테이블3 = new OrderTable(3L, 0, true);
        단체1 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(테이블1, 테이블2, 테이블3));
        테이블_GUEST3_NOT_EMPTY = new OrderTable(4L, 3, false);
        테이블_TABLEGROUP = new OrderTable(5L, 3, false);
    }

    @Test
    @DisplayName("단체을 등록한다.")
    void 단체_등록() {
        // given
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블1, 테이블2, 테이블3));
        given(tableGroupRepository.save(단체1)).willReturn(단체1);

        // when
        TableGroup saveTableGroup = tableGroupService.create(단체1);

        // then
        assertThat(saveTableGroup.getId()).isEqualTo(단체1.getId());
        assertThat(saveTableGroup.getCreatedDate()).isEqualTo(단체1.getCreatedDate());
        assertThat(saveTableGroup.getOrderTables()).isEqualTo(단체1.getOrderTables());
    }

    @Test
    @DisplayName("단체 내 주문테이블이 2개 미만이면 오류 발생한다.")
    void error_단체_등록_ONE_주문_테이블() {
        // given
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(테이블1));

        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("단체 내 주문테이블이 중복이면 오류 발생한다.")
    void error_단체_등록_중복_주문_테이블() {
        // given
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(테이블1, 테이블1));

        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("단체 내 주문테이블이 등록되어 있지 않으면 오류 발생한다.")
    void error_단체_등록_NOT_REGISTER_주문_테이블() {
        // given
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블1, 테이블2));

        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(단체1));
    }

    @Test
    @DisplayName("단체 내 주문테이블이 비어 있지 않으면 오류 발생한다.")
    void error_단체_등록_NOT_EMPTY_주문_테이블() {
        // given
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블1, 테이블_GUEST3_NOT_EMPTY));

        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(단체1));
    }

    @Test
    @DisplayName("다른 단체에 지정된 주문테이블이 있으면 등록 시, 오류 발생한다.")
    void error_단체_등록_ANOTHER_단체_주문_테이블() {
        // given
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블1, 테이블_TABLEGROUP));

        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(단체1));
    }

    @Test
    @DisplayName("단체을 삭제한다.")
    void 단체_삭제() {
        // given
        given(orderTableRepository.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(테이블1, 테이블2, 테이블3));

        // when
        tableGroupService.ungroup(단체1.getId());

        // then
        assertThat(테이블1.getTableGroup()).isNull();
        assertThat(테이블2.getTableGroup()).isNull();
        assertThat(테이블3.getTableGroup()).isNull();
    }

    @Test
    @DisplayName("단체 삭제 시, 주문 테이블의 상태가 주문 중 / 식사중 이면 오류 발생한다.")
    void error_단체_삭제_주문_테이블_상태() {
        // given
        given(orderTableRepository.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(테이블1, 테이블2, 테이블3));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.ungroup(단체1.getId()));
    }
}
