package mini_tracker.mini_tracker.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mini_tracker.mini_tracker.Entities.User;
import mini_tracker.mini_tracker.Exceptions.UnauthorizedException;
import mini_tracker.mini_tracker.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.UUID;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    UserService userService;


    // File: JWTFilter.java

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Inserire il token nell'authorization header nel formato giusto!");

        String accessToken= authHeader.replace("Bearer ", "");

        jwtTools.verifyToken(accessToken);

        UUID UserId = jwtTools.extractIdFromToken(accessToken);

        // ===========================================
        //  INIZIO BLOCCO DI DEBUG
        // ===========================================

        // CONTROLLO 1: Il service è stato iniettato correttamente?
        if (userService == null) {
            // Questo lancerà un 500 Internal Server Error con un messaggio chiaro
            throw new NullPointerException("ERRORE FATALE: UserService non è stato iniettato nel JWTFilter. Controlla le annotazioni.");
        }

        User found = userService.findById(UserId);

        // CONTROLLO 2: L'utente esiste ancora nel database?
        if (found == null) {
            // Questo lancerà un 401 Unauthorized con un messaggio chiaro
            throw new UnauthorizedException("ERRORE: L'utente (ID: " + UserId + ") non è stato trovato nel database, anche se il token è valido.");
        }

        // ===========================================
        //  FINE BLOCCO DI DEBUG
        // ===========================================


        Authentication authentication = new UsernamePasswordAuthenticationToken(
                found, null, found.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Ignora sia /auth/** SIA tutte le richieste OPTIONS
        return new AntPathMatcher().match("/auth/**", request.getServletPath()) ||
                request.getMethod().equalsIgnoreCase("OPTIONS");
    }
}
