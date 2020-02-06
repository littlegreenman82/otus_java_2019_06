package ru.otus.hw16database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw16database.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
