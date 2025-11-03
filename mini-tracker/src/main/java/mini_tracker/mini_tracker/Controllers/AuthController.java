package mini_tracker.mini_tracker.Controllers;

import mini_tracker.mini_tracker.Entities.User;
import mini_tracker.mini_tracker.Exceptions.ValidationsException;
import mini_tracker.mini_tracker.Payloads.LoginPayload;
import mini_tracker.mini_tracker.Payloads.LoginResponsePayload;
import mini_tracker.mini_tracker.Payloads.TokenPayload;
import mini_tracker.mini_tracker.Payloads.UserPayload;
import mini_tracker.mini_tracker.Services.AuthorizationService;
import mini_tracker.mini_tracker.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public LoginResponsePayload login(@RequestBody LoginPayload body){
        return authorizationService.CheckCredentialAndDoToken(body);
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUtente(@RequestBody @Validated UserPayload payload, BindingResult validationResult){
        if (validationResult.hasErrors()) {

            throw new ValidationsException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.userService.saveNewUser(payload);
    }

}
