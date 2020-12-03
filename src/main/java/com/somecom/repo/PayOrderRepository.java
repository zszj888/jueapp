package com.somecom.repo;

import com.somecom.entity.PayOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface PayOrderRepository extends JpaRepository<PayOrder, Integer>, JpaSpecificationExecutor<PayOrder> {
}