package com.nveapp.repository;

import java.util.Optional;

import com.nveapp.model.User;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    boolean validateUser(String email, String password);
}
