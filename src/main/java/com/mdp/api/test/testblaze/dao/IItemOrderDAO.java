package com.mdp.api.test.testblaze.dao;

import com.mdp.api.test.testblaze.entities.ItemOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IItemOrderDAO extends CrudRepository<ItemOrder, Long> {
    @Query("select p from ItemOrder p where p.id = ?1")
    ItemOrder fetchById(Long id);

}
