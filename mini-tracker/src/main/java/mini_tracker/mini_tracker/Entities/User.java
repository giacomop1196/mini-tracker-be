package mini_tracker.mini_tracker.Entities;

import jakarta.persistence.*;
import lombok.*;
import mini_tracker.mini_tracker.Enums.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @Generated
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
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
