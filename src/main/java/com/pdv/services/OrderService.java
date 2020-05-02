package com.pdv.services;

import com.pdv.entities.Order;
import com.pdv.entities.OrderItem;
import com.pdv.entities.Payment;
import com.pdv.entities.Product;
import com.pdv.entities.enums.OrderStatus;
import com.pdv.repositories.OrderItemRepository;
import com.pdv.repositories.OrderRepository;
import com.pdv.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private Order order = new Order(OrderStatus.WAITING_PAYMENT);


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
        return this.order = (Order) order;
    }

    public Payment addPayment(Payment payment){
        payment.setCodigo(order.getPayments().size()+1);
        order.getPayments().add(payment);
        return payment;
    }


}
