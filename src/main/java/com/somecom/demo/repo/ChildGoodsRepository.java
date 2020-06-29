package com.somecom.demo.repo;

import com.somecom.demo.entity.ChildGoods;
import org.springframework.data.repository.CrudRepository;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ChildGoodsRepository extends CrudRepository<ChildGoods, Integer> {

}