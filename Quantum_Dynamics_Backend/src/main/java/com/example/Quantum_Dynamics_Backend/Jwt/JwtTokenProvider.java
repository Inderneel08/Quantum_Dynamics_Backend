package com.example.Quantum_Dynamics_Backend.Jwt;

import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Component
public class JwtTokenProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    private SecretKey jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Value("${jwt.expirationMs}")
    private long jwtExpirationMs;

    public String generateToken(UserDetails userDetails) {

        Date now = new Date();

        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        System.out.println("Username is:"+userDetails.getUsername());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(jwtSecret, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

        return (String) (claims.getSubject());
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getExpiration();

        return (expiration.before(new Date()));
    }

    public boolean validateToken(String token) throws UsernameNotFoundException {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            final String username = getUsernameFromToken(token);

            System.out.println("Username :"+username);

            if (userDetailsService.loadUserByUsername(username).getUsername() != null) {
                return (true);
            } else {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }

        } catch (SignatureException exception) {
            System.out.println(exception);
            return false;
        } catch (MalformedJwtException exception) {
            System.out.println(exception);
            return false;
        } catch (ExpiredJwtException exception) {
            System.out.println(exception);
            return false;
        } catch (UnsupportedJwtException exception) {
            System.out.println(exception);
            return false;
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
            return false;
        }
    }
}
