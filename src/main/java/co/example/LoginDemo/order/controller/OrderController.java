package co.example.LoginDemo.order.controller;

import co.example.LoginDemo.order.entities.Order;
import co.example.LoginDemo.order.repository.OrderRepository;
import co.example.LoginDemo.order.service.OrderService;
import co.example.LoginDemo.user.entities.Roles;
import co.example.LoginDemo.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@PreAuthorize("hasRole('"+ Roles.REGISTERED + "')")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;


    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order){

        return ResponseEntity.ok(orderService.save(order));
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasRole(' " + Roles.ADMIN +"') OR returnObject.body == null OR returnObject.body.createdBy.id == authentication.principal.id")
    public ResponseEntity <Order> getSingle (@PathVariable Long id){
        Optional<Order> order = orderRepository.findById(id);
        if (!order.isPresent()) return  ResponseEntity.notFound().build();
        return  ResponseEntity.ok(order.get());
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = user.getRoles().stream().filter(role -> role.getName().equals(Roles.ADMIN)).findAny().isPresent();
        if (isAdmin) {
            return ResponseEntity.ok(orderRepository.findAll());
        } else {
            return ResponseEntity.ok(orderRepository.findByCreatedBy(user));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity <Order> update (@RequestBody Order order, @PathVariable Long id){
        if (!orderService.canEdit(id)){

        }
    return  ResponseEntity.ok(orderService.update(id, order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        if (!orderService.canEdit(id)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        orderRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
