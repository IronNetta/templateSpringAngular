package org.seba.services.user;

import org.seba.entities.User;
import org.seba.requests.SearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    Page<User> getUsers(List<SearchParam<User>> searchParams, Pageable pageable);

    User getUserByEmail(String email);

    User saveUser(User user);

    User updateUser(User user, String email);

    void deleteUser(Long id);

    User getUserById(Long id);
}
