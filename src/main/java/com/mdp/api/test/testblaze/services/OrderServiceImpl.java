package com.mdp.api.test.testblaze.services;

import com.mdp.api.test.testblaze.dao.IItemOrderDAO;
import com.mdp.api.test.testblaze.dao.IOrderDAO;
import com.mdp.api.test.testblaze.entities.ItemOrder;
import com.mdp.api.test.testblaze.entities.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderDAO orderDAO;

    @Autowired
    private IItemOrderDAO itemOrderDAO;

    @Override
    @Transactional(readOnly = true)
    public List<Order> listAll() {
        return (List<Order>) orderDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> listAll(Pageable pageable) {
        return orderDAO.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional()
    public Order saveOrder(Order order) {
        return orderDAO.save(order);
    }

    @Override
    @Transactional()
    public void deleteById(Long id) {
        orderDAO.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemOrder fetchById(Long id) {
        return itemOrderDAO.fetchById(id);
    }

    //@Override
    //@Transactional()
    //public void deleteByIdItemOrder(Long id) {
    //    itemOrderDAO.deleteByIdItemOrder(id);
    //}
}
