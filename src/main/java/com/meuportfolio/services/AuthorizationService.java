package com.meuportfolio.services;

import com.meuportfolio.domain.User;
import com.meuportfolio.dtos.RegisterRequest;
import com.meuportfolio.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
        String encryptedPassword = passwordEncoder.encode(request.password());
        User newUser = new User(
                request.username(),
                encryptedPassword,
                request.email(),
                request.role()
        );
        return this.userRepository.save(newUser);
    }
}