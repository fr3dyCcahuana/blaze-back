package com.mdp.api.test.testblaze.dao;

import com.mdp.api.test.testblaze.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderDAO extends JpaRepository<Order,Long> {
}
