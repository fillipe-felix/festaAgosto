package com.pdv.services;

import com.pdv.entities.Order;
import com.pdv.entities.OrderItem;
import com.pdv.entities.Product;
import com.pdv.repositories.OrderItemRepository;
import com.pdv.repositories.OrderRepository;
import com.pdv.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private Order venda = new Order();


    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        Optional<Order> obj = orderRepository.findById(id);
        return obj.get();
    }

    public Order save(Order venda) {
        orderRepository.save(venda);
        this.venda = venda;
        return venda;
    }

    public OrderItem getDesc(String desc) {
        Optional<Product> opProduto = produtoRepository.findByDesc(desc);
        OrderItem itemVenda = new OrderItem(opProduto.get());
        itemVenda.setQuantity(1);
        itemVenda.setPrice(opProduto.get().getPrice());
        itemVenda.setOrder(venda);
        orderItemRepository.save(itemVenda);
        return itemVenda;
    }

    public OrderItem getSubTotal(String desc, int qntd) {
        Optional<Product> opProduto = produtoRepository.findByDesc(desc);
        OrderItem itemVenda = new OrderItem(venda, opProduto.get(), qntd, opProduto.get().getPrice());
        itemVenda.getSubTotal();
        return itemVenda;
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    public Optional<Order> getVendaById(Long id) {
        return orderRepository.findById(id);
    }


}
