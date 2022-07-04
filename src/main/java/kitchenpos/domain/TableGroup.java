package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @Embedded
    private GroupOrderTables orderTables;

    protected TableGroup() {

    }

    public TableGroup(Long id, GroupOrderTables orderTables) {
        this(orderTables);
        createdDate = LocalDateTime.now();
        this.id = id;
    }

    public TableGroup(GroupOrderTables orderTables) {
        this.orderTables = orderTables;
        createdDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getTables();
    }

    public void group() {
        orderTables.group(id);
    }

    public void unGroup() {
        orderTables.unGroup();
    }
}
