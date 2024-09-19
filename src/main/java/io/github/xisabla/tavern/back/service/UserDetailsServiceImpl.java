package io.github.xisabla.tavern.back.service;

import io.github.xisabla.tavern.back.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service to handle user authentication details.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Load the user data from the database.
     *
     * @param username Username or email of the user
     * @return The user details
     * @throws UsernameNotFoundException If the user is not found
     * @note This method is a duplicate of the method in the UserService, but it is required to avoid a circular dependency.
     */
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .orElseGet(() -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username)));
    }
}
