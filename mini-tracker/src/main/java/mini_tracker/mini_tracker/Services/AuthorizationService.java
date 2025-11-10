package mini_tracker.mini_tracker.Services;

import mini_tracker.mini_tracker.Entities.User;
import mini_tracker.mini_tracker.Exceptions.UnauthorizedException;
import mini_tracker.mini_tracker.Payloads.LoginPayload;
import mini_tracker.mini_tracker.Payloads.LoginResponsePayload;
import mini_tracker.mini_tracker.Security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bcrypt;

    public LoginResponsePayload CheckCredentialAndDoToken(LoginPayload body) {

        User found = this.userService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), found.getPassword())) {
            if (!found.isAccountNonLocked()) {
                throw new UnauthorizedException("Questo account è bloccato.");
            }
            String token = jwtTools.createToken(found);
            return new LoginResponsePayload(token, found.getUserId());
        } else{
            throw new UnauthorizedException("credenziali errate");
        }

    }

}