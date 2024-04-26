package joboffers.infrastructure.security.jwt;

import joboffers.domain.loginandregister.LoginAndRegisterFacade;
import joboffers.domain.loginandregister.dto.UserResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

@AllArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {

    private final LoginAndRegisterFacade loginAndRegisterFacade;
    @Override
    public UserDetails loadUserByUsername(final String username) throws BadCredentialsException {
        final UserResponseDto username1 = loginAndRegisterFacade.findByUsername(username);
        return getUser(username1);
    }

    private org.springframework.security.core.userdetails.User getUser(UserResponseDto dto){
        String role = dto.isAdmin() ? "ADMIN" : "USER";
        return new org.springframework.security.core.userdetails.User(
                dto.username(),
                dto.password(),
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }
}
