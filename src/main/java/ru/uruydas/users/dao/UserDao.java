package ru.uruydas.users.dao;

import ru.uruydas.users.model.User;

public interface UserDao {

    boolean checkExists(String username);

    User getById(Long id);

    User getByUsername(String username);

    User create(User user);

    void update(User user);
}
