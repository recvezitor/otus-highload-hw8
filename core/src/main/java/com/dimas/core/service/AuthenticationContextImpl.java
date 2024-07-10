package com.dimas.core.service;

import com.dimas.core.domain.entity.AuthUser;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class AuthenticationContextImpl implements AuthenticationContext {
    private AuthUser user;

    @Override
    public AuthUser getCurrentUser() {
        return user;
    }

    public void setCurrentUser(AuthUser user) {
        this.user = user;
    }

}