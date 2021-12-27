package kitchenpos.order.dto;

import org.checkerframework.common.aliasing.qual.Unique;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class TableGroupRequest {
    @Unique
    @Size(min = 2)
    @NotEmpty
    private List<Long> tableIds;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> tableIds) {
        this.tableIds = tableIds;
    }

    public List<Long> getTableIds() {
        return tableIds;
    }
}
