package com.example.Quantum_Dynamics_Backend.Repository;

import java.math.BigInteger;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.Quantum_Dynamics_Backend.DAO.User;

@Repository
public interface UserRepository extends CrudRepository<User,BigInteger>{
    Optional<User>  findByUsername(String username);
}
