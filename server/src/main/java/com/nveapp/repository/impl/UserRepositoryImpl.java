package com.nveapp.repository.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.nveapp.model.User;
import com.nveapp.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private Map<String, User> users = new HashMap<>();

    public UserRepositoryImpl() {
        users.put("nerea@correo.com", new User("nerea@correo.com", "123456"));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(users.get(email));
    }

    @Override
    public boolean validateUser(String email, String password) {
        return users.containsKey(email) && users.get(email).getPassword().equals(password);
    }
}
