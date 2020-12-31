package com.somecom.repo;

import com.somecom.entity.PayOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface PayOrderRepository extends JpaRepository<PayOrder, Integer>, JpaSpecificationExecutor<PayOrder> {
    List<PayOrder> findByOwnerAndStatusEqualsAndCreateTimeBetween(Integer owner, byte status,LocalDateTime start,LocalDateTime end);
}