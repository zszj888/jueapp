package com.somecom.repo;

import com.somecom.entity.TaskApply;
import org.springframework.data.jpa.repository.JpaRepository;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface TaskApplyRepository extends JpaRepository<TaskApply, Integer> {
}