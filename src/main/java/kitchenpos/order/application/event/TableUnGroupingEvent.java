package kitchenpos.order.application.event;

import java.util.List;

public class TableUnGroupingEvent {
    private final Long tableGroupId;
    private final List<Long> tableIds;

    public TableUnGroupingEvent(Long tableGroupId, List<Long> tableIds) {
        this.tableGroupId = tableGroupId;
        this.tableIds = tableIds;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<Long> getTableIds() {
        return tableIds;
    }
}
