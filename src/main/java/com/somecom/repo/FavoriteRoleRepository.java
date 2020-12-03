package com.somecom.repo;

import com.somecom.entity.FavoriteRole;
import com.somecom.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface FavoriteRoleRepository extends JpaRepository<FavoriteRole, Integer> {
    @Transactional
    void deleteByUserIdAndRoleIs(Integer userId, Role role);
}