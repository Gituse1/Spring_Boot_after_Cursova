package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Шукаємо користувача в нашій базі по email
        User myUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Користувача з email " + email + " не знайдено"));

        // 2. Створюємо об'єкт User, який розуміє Spring Security
        // Важливо: тут ми використовуємо клас User від Spring Security, а не наш!
        return org.springframework.security.core.userdetails.User.builder()
                .username(myUser.getEmail())      // В якості логіна використовуємо email
                .password(myUser.getPassword())   // Пароль з бази (у нас там {noop}1234)
                .build();
    }
}
