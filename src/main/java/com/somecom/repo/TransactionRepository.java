package com.somecom.repo;

import com.somecom.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface TransactionRepository extends JpaRepository<Transaction, Integer>, JpaSpecificationExecutor<Transaction> {
}