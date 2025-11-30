package com.meuportfolio.services;

import com.meuportfolio.domain.Role;
import com.meuportfolio.domain.User;
import com.meuportfolio.dtos.RegisterRequest;
import com.meuportfolio.repositories.RoleRepository;
import com.meuportfolio.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }
        return user;
    }

    public User register(RegisterRequest request){
        if(this.userRepository.findByUsername(request.username()) != null){
            throw new RuntimeException("Usuário já existe");
        }
        Optional<Role> roleOptional = roleRepository.findByName(request.roleName());
        if (roleOptional.isEmpty()) {
            throw new RuntimeException("Role não encontrada: " + request.roleName());
        }
        Set<Role> roles = new HashSet<>();
        roles.add(roleOptional.get());

        String encryptedPassword = passwordEncoder.encode(request.password());
        User newUser = new User(
                request.username(),
                encryptedPassword,
                request.email(),
                roles
        );
        return this.userRepository.save(newUser);
    }
}