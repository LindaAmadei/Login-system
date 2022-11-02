package co.example.LoginDemo.order.repository;

import co.example.LoginDemo.order.entities.Order;
import co.example.LoginDemo.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCreatedBy (User user);
}
