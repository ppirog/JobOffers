package joboffers.infrastructure.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@AllArgsConstructor
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private final JwtConfigurationProperties properties;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getUserNamePasswortAuthenticationToken(authorization);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getUserNamePasswortAuthenticationToken(final String token) {
        String secretKey = properties.secret();

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();

        // this substring method is to remove phrase "Bearer " from the token
        DecodedJWT jwt = verifier.verify(token.substring(7));
        String role = jwt.getClaim("role").asString();

        return new UsernamePasswordAuthenticationToken(jwt.getSubject(), null, Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role)));
    }
}
