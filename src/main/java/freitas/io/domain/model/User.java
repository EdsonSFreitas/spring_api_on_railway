package freitas.io.domain.model;

import freitas.io.enums.RolesEnum;
import freitas.io.security.password.PasswordComplexity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 03/10/2023
 * {@code @project} api
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity(name = "tb_user")
@NoArgsConstructor(force = true)
@DynamicUpdate
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotEmpty(message = "{field.login.obrigatorio}")
    private String login;

    @NotEmpty(message = "{field.senha.obrigatorio}")
    @PasswordComplexity(minLength = 3, requireLowerCase = true, requireUpperCase = true, requireSpecialChar = true, requireNumber = true, message = "{field.senha.complexidade}")
    private String password;

    @NotBlank
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private Account account;

    @OneToOne(cascade = CascadeType.ALL)
    private Card card;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Feature> features;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<News> news;

    @Column(name = "is_enabled")
    private boolean isEnabled = true;

    @Column(name = "is_account_locked")
    private boolean isAccountLocked = false;

    @Column(name = "credentials_expiration")
    private LocalDateTime credentialsExpiration;

    @Column(name = "account_expiration")
    private LocalDateTime accountExpiration;

    private Integer role;


    public RolesEnum getRole() {
        return RolesEnum.valueOf(role);
    }

    public void setRole(RolesEnum role) {
        if (role != null) {
            this.role = role.getCode();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getRole().toString()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountExpiration == null || !accountExpiration.isBefore(LocalDateTime.now());
        /*if (accountExpiration == null) {
            return true;
        } else {
            LocalDateTime now = LocalDateTime.now();
            if (accountExpiration.isBefore(now)) {
                return false;
            } else {
                return true;
            }
        }*/
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isAccountLocked;
        /*if (isAccountLocked) {
            return false;
        } else {
            return true;
        }*/
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsExpiration == null || !credentialsExpiration.isBefore(LocalDateTime.now());
        /*if (credentialsExpiration == null) {
            return true;
        } else {
            LocalDateTime now = LocalDateTime.now();
            if (credentialsExpiration.isBefore(now)) {
                return false;
            } else {
                return true;
            }
        }*/
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
        /*if (isEnabled) {
            return true;
        } else {
            return false;
        }*/
    }

    public void setAccountExpiration(LocalDateTime accountExpiration) {
        this.accountExpiration = accountExpiration;
    }

    public void setAccountLocked(boolean accountLocked) {
        isAccountLocked = accountLocked;
    }

    public void setCredentialsExpiration(LocalDateTime credentialsExpiration) {
        this.credentialsExpiration = credentialsExpiration;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}