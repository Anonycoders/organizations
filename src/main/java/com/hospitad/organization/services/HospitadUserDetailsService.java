package com.hospitad.organization.services;

import com.hospitad.organization.models.User;
import com.hospitad.organization.repositories.UserRepository;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class HospitadUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public HospitadUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        final User user;
        if (new EmailValidator().isValid(emailOrUsername, null)) {
            user = userRepository.findByEmail(emailOrUsername)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "No user found with email address: " + emailOrUsername));
        } else {
            user = userRepository.findByUsername(emailOrUsername)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "No user found with username: " + emailOrUsername));
        }

        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                List.of());
    }

}
