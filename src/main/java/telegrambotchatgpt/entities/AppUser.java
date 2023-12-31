package telegrambotchatgpt.entities;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "app_users")
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "telegram_user_id", nullable = false)
    private Long telegramUserId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Roles role;

    @Column(insertable = false, name = "status", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'INACTIVE'")
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        ACTIVE, PENDING, INACTIVE
    }

    public enum Roles {
        USER, ADMIN
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));
        return authorities;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}