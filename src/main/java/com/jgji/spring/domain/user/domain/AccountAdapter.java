package com.jgji.spring.domain.user.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;

public class AccountAdapter extends org.springframework.security.core.userdetails.User {

    private User account;

    private AccountAdapter(User account) {
        super(account.getUsername(), account.getPassword(), authorities());
        this.account = account;
    }

    public static AccountAdapter from(User account) {
        return new AccountAdapter(account);
    }

    private static Collection<? extends GrantedAuthority> authorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public User getAccount() {
        return this.account;
    }
}
