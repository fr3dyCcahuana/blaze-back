package com.mdp.api.test.testblaze.services;

import com.mdp.api.test.testblaze.entities.ItemOrder;
import com.mdp.api.test.testblaze.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional()
public interface IOrderService {
    List<Order> listAll();

    Page<Order> listAll(Pageable pageable);

    Order findById(Long id);

    Order saveOrder(Order order);

    void deleteById(Long id);

    ItemOrder fetchById(Long id);

    //void deleteByIdItemOrder(Long id);
}
