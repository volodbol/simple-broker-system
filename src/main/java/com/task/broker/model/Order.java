package com.task.broker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "order_t")
public class Order {

    @Id
    @GeneratedValue(generator = "order-uuid-gen")
    @GenericGenerator(name = "order-uuid-gen", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @OneToOne
    private ApplicationUser applicationUser;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @ManyToOne
    @JoinColumn(name = "order_instrument_id")
    private OrderInstrument orderInstrument;

    private BigDecimal amount;

    private BigDecimal price;

    private Boolean isSessionActive;

    private LocalDateTime createdAt;

    @PrePersist
    private void onPrePersist() {
        setCreatedAt(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (!id.equals(order.id)) return false;
        if (orderType != order.orderType) return false;
        return createdAt.equals(order.createdAt);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + orderType.hashCode();
        result = 31 * result + createdAt.hashCode();
        return result;
    }

}
