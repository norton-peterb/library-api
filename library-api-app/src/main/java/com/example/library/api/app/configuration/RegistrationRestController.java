package com.example.library.api.app.configuration;

import com.example.library.api.app.bean.RegisterRequest;
import com.example.library.api.app.bean.RegisterResponse;
import com.example.library.api.app.bean.UserLogin;
import com.example.library.api.app.dao.UserLoginDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationRestController {

    private final UserLoginDAO userLoginDAO;
    private final PasswordEncoder passwordEncoder;

    public RegistrationRestController(@Autowired UserLoginDAO userLoginDAO,
                                      @Autowired PasswordEncoder passwordEncoder) {
        this.userLoginDAO = userLoginDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(path = "/register",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        RegisterResponse registerResponse = new RegisterResponse("OK");
        userLoginDAO.getUserForUsername(request.getUsername()).ifPresentOrElse(
                userLogin -> registerResponse.setMessage(
                        String.format("User %s already exists, please choose another"
                            ,userLogin.getUsername())
                ),
                () -> {
                    UserLogin userLogin = new UserLogin();
                    userLogin.setUsername(request.getUsername());
                    userLogin.setPassword(passwordEncoder.encode(request.getPassword()));
                    userLogin.setRole("ROLE_USER");
                    userLogin.setEmail(request.getEmail());
                    userLoginDAO.createNewUser(userLogin);
                }
        );
        return registerResponse;
    }
}
