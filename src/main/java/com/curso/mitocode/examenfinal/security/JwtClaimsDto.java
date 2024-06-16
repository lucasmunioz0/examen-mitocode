package com.curso.mitocode.examenfinal.security;

import java.util.List;

public record JwtClaimsDto(String username, List<String> roles) {
}
