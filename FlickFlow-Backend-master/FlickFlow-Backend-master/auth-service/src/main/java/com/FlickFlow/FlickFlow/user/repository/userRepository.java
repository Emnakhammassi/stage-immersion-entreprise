package com.FlickFlow.FlickFlow.user.repository;

import com.FlickFlow.FlickFlow.user.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface userRepository extends JpaRepository<user, Integer>, CrudRepository<user, Integer> {
    user findByUsername(String username);
    user findByEmail(String email);

    user findByUserId(int id);
//    @Query("SELECT u FROM user u JOIN u.roles r WHERE r.roleId = :roleId")
//    List<user> findUsersByRole(@Param("roleId") int roleId);
////    @Override
//    void deleteById(Integer integer);

//    user findByUserameAndPassword(String username, String password);


}
