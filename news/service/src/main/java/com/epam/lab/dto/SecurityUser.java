package com.epam.lab.dto;

import com.epam.lab.model.Role;
import com.epam.lab.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class SecurityUser implements UserDetails {

    private String login;
    private String password;
    private List<GrantedAuthority> authorities;

    public SecurityUser(User user) {
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.authorities = convertRolesToAuthorities(user.getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.login;
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

    private List<GrantedAuthority> convertRolesToAuthorities(Set<Role> roles) {
        List<GrantedAuthority> userAuthorities = new ArrayList<>();
        for (Role role : roles) {
            userAuthorities.add(() -> "ROLE_" + role.getName());
        }
        return userAuthorities;
    }
}
