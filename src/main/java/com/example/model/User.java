package com.example.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "app_users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;


    @Column(nullable = false)
    private String role;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getUsername() {
        // ВАЖЛИВО: Вашим логіном є email
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // --- BOOLEAN FLAGS (Потрібні Spring Security) ---

    @Override
    public boolean isAccountNonExpired() {
        return true; // Акаунт дійсний
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Акаунт не заблокований
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Пароль дійсний
    }

    @Override
    public boolean isEnabled() {
        return isActive; // Зв'язуємо з вашим полем в БД!
    }
}