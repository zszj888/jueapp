package com.somecom.repo;

import com.somecom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface NotifyMessageRepository extends JpaRepository<User, Integer> {

}