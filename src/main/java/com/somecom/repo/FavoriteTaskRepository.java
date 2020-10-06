package com.somecom.repo;

import com.somecom.entity.FavoriteTask;
import org.springframework.data.jpa.repository.JpaRepository;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface FavoriteTaskRepository extends JpaRepository<FavoriteTask, Integer> {

}