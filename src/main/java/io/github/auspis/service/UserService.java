package io.github.auspis.service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.github.auspis.domain.dto.UserRequest;
import io.github.auspis.domain.dto.UserResponse;
import io.github.auspis.domain.entity.User;
import io.github.auspis.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> findAll() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(this::toResponse)
                .toList();
    }

    public UserResponse findById(Long id) {
        return userRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
    }

    public UserResponse create(UserRequest request) {
        User user = new User(request.getName(), request.getEmail(), request.getAge());
        user.setId(ThreadLocalRandom.current().nextLong(1, Integer.MAX_VALUE));
        return toResponse(userRepository.save(user));
    }

    public UserResponse update(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        return toResponse(userRepository.save(user));
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getAge());
    }
}
