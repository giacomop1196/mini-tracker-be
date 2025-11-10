package mini_tracker.mini_tracker.Entities;

import jakarta.persistence.*;
import lombok.*;
import mini_tracker.mini_tracker.Enums.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    UUID userId;

    @Enumerated(EnumType.STRING)
    UserType role;

    String username;
    String email;
    String password;
    String name;
    String surname;

    @Column(name = "avatar_url")
    private String avatarURL;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean locked = false;

    public User( String username, String email, String password, String name, String surname) {
        this.role = UserType.USER;
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // La tua versione (corretta)
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        // Modifica in 'true'
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Collega il nostro campo 'locked'
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Modifica in 'true'
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Modifica in 'true'
        return true;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Revenue> revenues;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Expense> expenses;
}