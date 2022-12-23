package com.task.broker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "order_agreement")
public class OrderAgreement {

    @Id
    @GeneratedValue(generator = "order-agree-uuid-gen")
    @GenericGenerator(name = "order-agree-uuid-gen", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @OneToOne
    private ApplicationUser applicationUser;

    @OneToOne
    private Order order;

    private Boolean isPerformed;

    private Boolean isCancelled;

    private LocalDateTime createdAt;

    @PrePersist
    private void onPrePersist() {
        setCreatedAt(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderAgreement that = (OrderAgreement) o;

        if (!id.equals(that.id)) return false;
        if (!applicationUser.equals(that.applicationUser)) return false;
        if (!order.equals(that.order)) return false;
        return Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + applicationUser.hashCode();
        result = 31 * result + order.hashCode();
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        return result;
    }

}
