package com.pdv.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pdv.entities.enums.OrderStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tb_order")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant moment;
    private Integer orderStatus;
    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;
    @OneToMany(mappedBy = "id.order")
    private Set<OrderItem> items = new HashSet<>();
    @OneToMany(mappedBy = "order")
    private List<Payment> payments = new ArrayList<Payment>();
    private Double remainingValue;
    private Double troco = 0D;


    public Order() {
    }

    public Order(OrderStatus orderStatus) {
        this.setOrderStatus(orderStatus);
    }

    public Order(Long id, Instant moment, OrderStatus orderStatus, User client) {
        this.id = id;
        this.moment = moment;
        setOrderStatus(orderStatus);
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public OrderStatus getOrderStatus() {
        return OrderStatus.valueOf(orderStatus);
    }

    public Double getTroco() {
        return troco;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        if (orderStatus != null) {
            this.orderStatus = orderStatus.getCode();
        }

    }

    public Double getRemainingValue() {
        double valuesPayments = payments.stream().mapToDouble(Payment::getValue).sum();
        if (this.items.size() == 0) {
            return this.getTotal();
        } else if (valuesPayments > getTotal()) {
            return this.remainingValue = 0D;

        } else {
            return this.remainingValue = getTotal() - valuesPayments;

        }
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setItems(Set<OrderItem> items) {
        this.items = items;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public Double getTotal() {
        double sum = 0.0;

        for (OrderItem x : items) {
            sum = sum + x.getSubTotal();
        }
        return sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return id != null ? id.equals(order.id) : order.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
