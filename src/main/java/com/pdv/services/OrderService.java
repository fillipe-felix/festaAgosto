package com.pdv.services;

import com.pdv.entities.Order;
import com.pdv.entities.OrderItem;
import com.pdv.entities.Payment;
import com.pdv.entities.Product;
import com.pdv.entities.enums.OrderStatus;
import com.pdv.entities.enums.TypePayment;
import com.pdv.repositories.OrderItemRepository;
import com.pdv.repositories.OrderRepository;
import com.pdv.repositories.PaymentRepository;
import com.pdv.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository produtoRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    private Order order = new Order(OrderStatus.WAITING_PAYMENT);
    private double remainingValue = 0;
    private double totalValue = 0;
    private boolean existTroco = false;
    private boolean firtPayment = false;


    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        Optional<Order> obj = orderRepository.findById(id);
        return obj.get();
    }

    public Order save(Order order) {
        orderRepository.save(order);
        this.order = order;
        return order;
    }

    public Order closeOrder() {
        order.setOrderStatus(OrderStatus.SHIPPED);
        order.setMoment(Instant.now());
        order.getItems().stream().forEach(a -> {
            a.setOrder(order);
        });
        orderRepository.save(order);
        orderItemRepository.saveAll(order.getItems());
        order.getPayments().stream().forEach(a -> {
            a.setOrder(order);
        });
        paymentRepository.saveAll(order.getPayments());
        return order;
    }

    public void delete(String desc) {
        removeItemVenda(desc);
    }

    //add OrderItem WITH description
    public OrderItem getDesc(String desc) {
        Optional<Product> optionalProduct = produtoRepository.findByDesc(desc);
        OrderItem orderItem = new OrderItem(optionalProduct.get());
        if (checkIfExistsOrderItem(orderItem.getProduct())) {
            orderItem.setQuantity(1);
            orderItem.setPrice(optionalProduct.get().getPrice());
            order.getItems().add(orderItem);
            return orderItem;
        }
        return null;
    }

    public OrderItem getSubTotal(String desc, int quantity) {
        Optional<Product> optionalProduct = produtoRepository.findByDesc(desc);
        OrderItem orderItem = attQuantidadeItemVenda(optionalProduct.get(), quantity);
        orderItem.getSubTotal();
        return orderItem;
    }


    private OrderItem attQuantidadeItemVenda(Product product, int quantity) {
        for (OrderItem oi : order.getItems()) {
            if (oi.getProduct().getName().equals(product.getName())) {
                oi.setQuantity(quantity);
                return oi;
            }
        }
        return null;
    }

    private Boolean checkIfExistsOrderItem(Product product) {
        for (OrderItem oi : order.getItems()) {
            if (oi.getProduct().getName().equals(product.getName())) {
                return false;
            }
        }
        return true;
    }

    private Boolean removeItemVenda(String name) {
        for (OrderItem oi : order.getItems()) {
            if (oi.getProduct().getName().equals(name)) {
                order.getItems().remove(oi);
                return true;
            }
        }
        return false;
    }


    public Order getTotal() {
        order.getRemainingValue();
        return this.order;
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    public Optional<Order> getVendaById(Long id) {
        return orderRepository.findById(id);
    }

    public Order orderClean() {
        Order order = new Order(OrderStatus.CANCELED);
        remainingValue = 0;
        existTroco = false;
        return this.order = (Order) order;
    }

    public Boolean addPayment(Payment payment) {
        if (order.getRemainingValue() > 0) {
            existTroco = false;
        }
        sumAllPayments();
        if (order.getPayments().size() == 0 && payment.getValue() > order.getTotal() - sumAllPayments()) {
            firtPayment = true;
        }
        if (order.getTotal() - sumAllPayments() == 0 || existTroco && order.getTotal() - sumAllPayments() > 0) {
            return false;
        }
        if (payment.getValue() >= order.getTotal() - sumAllPayments() && firtPayment && TypePayment.Dinheiro.equals(payment.getTypePayment()) && !existTroco) {
            calculaTroco(payment);
            payment.setCodigo(order.getPayments().size() + 1);
            payment.setRemainingValue(0.0);
            payment.setTotalPago((sumAllPayments() + payment.getValue()) - payment.getTroco());
            order.getPayments().add(payment);
            existTroco = true;
            return true;
        } else if (payment.getValue() <= order.getTotal() - sumAllPayments() && TypePayment.Dinheiro.equals(payment.getTypePayment())) {
            calculaRemainingValue();
            payment.setCodigo(order.getPayments().size() + 1);
            payment.setTotalPago(sumAllPayments() + payment.getValue());
            payment.setRemainingValue(order.getTotal() - (sumAllPayments() + payment.getValue()));
            order.getPayments().add(payment);
            existTroco = false;
            return true;
        } else if (payment.getValue() <= order.getTotal() - sumAllPayments() && !TypePayment.Dinheiro.equals(payment.getTypePayment())) {
            calculaRemainingValue();
            payment.setCodigo(order.getPayments().size() + 1);
            payment.setTotalPago(sumAllPayments() + payment.getValue());
            payment.setRemainingValue(order.getTotal() - (sumAllPayments() + payment.getValue()));
            order.getPayments().add(payment);
            existTroco = false;
            return true;
        } else if (payment.getValue() > order.getTotal() - sumAllPayments() && TypePayment.Dinheiro.equals(payment.getTypePayment()) && !existTroco) {
            calculaTroco(payment);
            payment.setCodigo(order.getPayments().size() + 1);
            payment.setRemainingValue(0.0);
            payment.setTotalPago((sumAllPayments() + payment.getValue()) - payment.getTroco());
            order.getPayments().add(payment);
            return true;
        } else {
            return false;
        }
    }


    private double sumAllPayments() {
        return totalValue = order.getPayments().stream().mapToDouble(Payment::getValue).sum();
    }

    private void calculaTroco(Payment payment) {
        payment.setTroco((totalValue + payment.getValue()) - order.getTotal());
    }

    private void calculaRemainingValue() {
        remainingValue = order.getTotal() - sumAllPayments();
    }


}
