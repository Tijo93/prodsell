package com.ss.prodsel.config.security;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@SecurityRequirement(name = "basicAuth")
@PreAuthorize("hasRole('ADMIN')")
public @interface IsAdmin {
}
