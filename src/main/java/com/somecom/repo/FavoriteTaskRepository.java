package com.somecom.repo;

import com.somecom.entity.FavoriteTask;
import com.somecom.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface FavoriteTaskRepository extends JpaRepository<FavoriteTask, Integer> {
    @Transactional
    void deleteByUserIdAndTaskIs(Integer userId, Task task);
}