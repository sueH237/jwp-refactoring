package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Menu menu, long quantity) {
        this(null, menu, quantity);
    }

    public OrderLineItem(Long seq, Menu menu, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
