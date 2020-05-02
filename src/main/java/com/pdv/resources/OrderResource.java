package com.pdv.resources;

import com.pdv.entities.Order;
import com.pdv.entities.OrderItem;
import com.pdv.entities.User;
import com.pdv.entities.enums.OrderStatus;
import com.pdv.services.OrderService;
import com.pdv.services.ProductService;
import com.pdv.services.UserService;
import com.pdv.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/orders")
public class OrderResource {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(OrderResource.class);

    @GetMapping
    public ResponseEntity<List<Order>> findAll() {
        List<Order> list = orderService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("venda/venda");
        return modelAndView;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id) {
        Order obj = orderService.findById(id);
        return ResponseEntity.ok().body(obj);
    }


    @PostMapping("/add")
    public ResponseEntity<Order> register(@Valid @RequestBody final Order order) {
        Optional<User> optionalUser = Optional.ofNullable(userService.findById(1L));
        Order v = new Order(null, Instant.now(), OrderStatus.PAID, optionalUser.get());
        orderService.save(v);
        return ResponseEntity.ok().body(v);
    }


    @RequestMapping("/getById")
    @ResponseBody
    public Optional<Order> getById(final Long id) {
        final Optional<Order> orderItem = orderService.getVendaById(id);
        return orderItem;
    }

    @RequestMapping("/getDesc")
    @ResponseBody
    public OrderItem getDesc(final String desc) {
        OrderItem orderItem = orderService.getDesc(desc);
        return orderItem;
    }

    @RequestMapping(path = "/subtotal/{desc}/{qntd}", method = RequestMethod.GET)
    @ResponseBody
    public OrderItem getSubtotal(@PathVariable final String desc, @PathVariable final int qntd) {
        return orderService.getSubTotal(desc, qntd);
    }


}
