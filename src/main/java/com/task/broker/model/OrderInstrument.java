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
import javax.persistence.Table;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "order_instrument")
public class OrderInstrument {

    @Id
    @GeneratedValue(generator = "order-inst-uuid-gen")
    @GenericGenerator(name = "order-inst-uuid-gen", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String instrumentName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderInstrument that = (OrderInstrument) o;

        if (!id.equals(that.id)) return false;
        return instrumentName.equals(that.instrumentName);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + instrumentName.hashCode();
        return result;
    }

}
