package com.example.apigravadora.model.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity  //Anotação que indica que classe é uma entidade.
@Data //Anotação que cria automaticamente o Get e Set do atributo.
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "users_table") // Anotação que denomina noma da tabela do BD.

public class Usuario implements UserDetails {

    @Id //Anotação para identificar como PK a coluna da tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //A geração do ID sera automatica e
    private Long id;

    @NotBlank (message = "O campo login é obrigatorio")
    @Column(name = "username", unique = true, nullable = false)
    private String login;

    @NotBlank (message = "O campo password é obrigatorio")
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private UserRole role;

    public Usuario(String login, String password, UserRole role){
        this.login = login;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("Role_USER"));
        else return List.of(new SimpleGrantedAuthority("Role_USER"));
    }

    public String getUsername(){
        return login;
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
