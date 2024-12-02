package com.example.be_datn.config;

import com.example.be_datn.entity.TaiKhoan;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@Transactional
public class AccountDetailsImpl implements UserDetails {
    TaiKhoan account;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
        roles.add(new SimpleGrantedAuthority(account.getVaiTro().getTenVaiTro()));
        return roles;
    }

    public TaiKhoan getAccount() {
        return this.account;
    }

    @Override
    public String getPassword() {
        if (account == null)
            return null;
        if (account.getMatKhau() == null)
            return null;
        return account.getMatKhau();
    }

    @Override
    public String getUsername() {
        if (account == null)
            return null;
        if (account.getEmail() == null)
            return null;
        return account.getEmail();
    }

    public String getRoleName() {
        return account.getVaiTro().getTenVaiTro();
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
