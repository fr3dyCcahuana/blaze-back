package com.mdp.api.test.testblaze.controllers;

import com.mdp.api.test.testblaze.entities.ItemOrder;
import com.mdp.api.test.testblaze.entities.Order;
import com.mdp.api.test.testblaze.entities.State;
import com.mdp.api.test.testblaze.services.IOrderService;
import com.mdp.api.test.testblaze.services.IProductService;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IProductService productService;

    @GetMapping
    public ResponseEntity<?> showOrders() {

        List<Order> orders = null;
        Map<String, Object> map = new HashMap<>();

        try {
            orders = orderService.listAll();
        } catch (DataAccessException e) {
            map.put("message", "Error en la consulta");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
    }

    @GetMapping("/page/{page}")
    public ResponseEntity<?> showOrders(@PathVariable(value = "page") Long page) {

        Page<Order> orders = null;
        Pageable pageable = null;
        Map<String, Object> map = new HashMap<>();

        try {
            pageable = PageRequest.of(page.intValue() - 1, 2);
            orders = orderService.listAll(pageable);
        } catch (DataAccessException e) {
            map.put("message", "Error en la consulta");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Page<Order>>(orders, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<?> showProductById(@PathVariable(value = "id") Long id) {

        Order order = null;
        Map<String, Object> map = new HashMap<>();

        try {
            order = orderService.findById(id);
        } catch (DataAccessException e) {
            map.put("message", "Error en la consulta");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.BAD_REQUEST);
        }
        if (order == null) {
            map.put("message", "Orden no encontrada en la BD");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> saveOrder(@RequestBody Order orderRequest) {

        Order order = null;
        Map<String, Object> map = new HashMap<>();

        try {
            order = orderService.saveOrder(orderRequest);

        } catch (DataException d) {
            map.put("message", "Error al insertar una Orden");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.BAD_REQUEST);
        }
        if (order == null) {
            map.put("message", "Orden no encontrada en la BD");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }

    @PostMapping(value = "{id}")
    public ResponseEntity<?> saveOrder(@PathVariable(value = "id") Long id, @RequestBody ItemOrder itemOrderRequest) {

        Order order = null;
        Order orderAux = null;
        ItemOrder itemOrder = new ItemOrder();
        Map<String, Object> map = new HashMap<>();

        try {
            orderAux = orderService.findById(id);

            itemOrder.setQuantity(itemOrderRequest.getQuantity());
            itemOrder.setProduct(itemOrderRequest.getProduct());

            orderAux.addOrderItem(itemOrder);
            order = orderService.saveOrder(orderAux);

        } catch (DataException d) {
            map.put("message", "Error al insertar una Orden");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.BAD_REQUEST);
        }
        if (order == null) {
            map.put("message", "Orden no encontrada en la BD");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable(value = "id") Long id) {

        Map<String, Object> map = new HashMap<>();

        try {
            orderService.deleteById(id);
        } catch (DataException d) {
            map.put("message", "Error al eliminar una Orden");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}/item/{itemId}")
    public ResponseEntity<?> deleteOrder(@PathVariable(value = "id") Long id, @PathVariable(value = "itemId") Long itemId) {

        Order order = null;
        ItemOrder itemOrder = new ItemOrder();
        Map<String, Object> map = new HashMap<>();

        try {
            order = orderService.findById(id);
            itemOrder = orderService.fetchById(itemId);
            for (int i = 0; i < order.getItemOrders().size(); i++) {
                if (order.getItemOrders().get(i).getId().equals(itemOrder.getId())) {
                    order.deleteOrderItem(itemOrder);
                    orderService.saveOrder(order);
                    break;
                }
            }


        } catch (DataException d) {
            map.put("message", "Error al eliminar una Orden");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<?> updateOrder(@PathVariable(value = "id") Long id, @RequestBody Order orderRequest) {

        Order order = null;
        Order orderResponse = null;
        Map<String, Object> map = new HashMap<>();

        try {
            order = orderService.findById(id);
            if (this.validateOrder(orderRequest.getState())) {
                order.setState((orderRequest.getState() != null) ? orderRequest.getState() : order.getState());
            }
            order.setCustomer((orderRequest.getCustomer() != null) ? orderRequest.getCustomer() : order.getCustomer());

            orderResponse = orderService.saveOrder(order);
        } catch (DataException d) {
            map.put("message", "Error al modificar una Orden");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.BAD_REQUEST);
        }
        if (order == null) {
            map.put("message", "Orden no encontrada en la BD");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Order>(orderResponse, HttpStatus.OK);
    }

    @PutMapping(value = "{id}/item/{itemId}")
    public ResponseEntity<?> updateOrder(@PathVariable(value = "id") Long id, @PathVariable(value = "itemId") Long itemId, @RequestBody ItemOrder itemOrderRequest) {

        Order order = null;
        Order orderResponse = null;
        Map<String, Object> map = new HashMap<>();
        ItemOrder itemOrder = null;
        try {
            order = orderService.findById(id);
            itemOrder = orderService.fetchById(itemId);

            for (int i = 0; i < order.getItemOrders().size(); i++) {
                if (order.getItemOrders().get(i).getId().equals(itemOrder.getId())) {
                    order.getItemOrders().get(i).setQuantity(itemOrderRequest.getQuantity());
                    break;
                }
            }
            orderResponse = orderService.saveOrder(order);
        } catch (DataException d) {
            map.put("message", "Error al modificar una Orden");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.BAD_REQUEST);
        }
        if (order == null) {
            map.put("message", "Orden no encontrada en la BD");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NOT_FOUND);
        }
        if (itemOrder == null) {
            map.put("message", "Item no encontrado en la BD");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Order>(orderResponse, HttpStatus.OK);
    }

    private boolean validateOrder(String order) {
        if (order.equals(State.PENDING.toString())) return false;

        return true;
    }
}
