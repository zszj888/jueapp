package com.somecom.demo.repo;

import com.somecom.demo.entity.DemoT;
import com.somecom.demo.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface DemoRepository extends JpaRepository<DemoT, Integer> {

}