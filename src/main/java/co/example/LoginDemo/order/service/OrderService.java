package co.example.LoginDemo.order.service;

import co.example.LoginDemo.order.entities.BaseEntity;
import co.example.LoginDemo.order.entities.Order;
import co.example.LoginDemo.order.repository.OrderRepository;
import co.example.LoginDemo.user.entities.User;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    private co.example.LoginDemo.user.entities.User User;

    public Order save (Order orderInput){
        if (orderInput == null) return null;
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderInput.setId(null);
        orderInput.setCreatedAt(LocalDateTime.now());
        orderInput.setCreatedBy(User);
        return orderRepository.save(orderInput);
    }

    public Order update (Long id, Order orderInput){
        if (orderInput == null) return null;
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderInput.setId(id);
        orderInput.setUpdateAt(LocalDateTime.now());
        orderInput.setUpdateBy(User);
        return orderRepository.save(orderInput);
    }

    public boolean canEdit(Long id){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Order> order = orderRepository.findById(id);
        if (!order.isPresent()) return false;
        return order.get().getCreatedBy().getId() == user.getId();
    }

}
