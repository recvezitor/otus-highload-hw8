package com.dimas.core.service;

import com.dimas.core.domain.entity.AuthUser;

public interface AuthenticationContext {
    AuthUser getCurrentUser();

}