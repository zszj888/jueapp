package com.somecom.repo;

import com.somecom.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface TaskRepository extends JpaRepository<Task, Integer> {

}