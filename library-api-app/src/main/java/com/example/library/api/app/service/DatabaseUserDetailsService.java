package com.example.library.api.app.service;

import com.example.library.api.app.bean.UserLogin;
import com.example.library.api.app.dao.UserLoginDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class DatabaseUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUserDetailsService.class);

    private final UserLoginDAO userLoginDAO;

    public DatabaseUserDetailsService(UserLoginDAO userLoginDAO) {
        this.userLoginDAO = userLoginDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("User Details requested for {}",username);
        Optional<UserLogin> userLogin = userLoginDAO.getUserForUsername(username);
        if(userLogin.isPresent()) {
            UserLogin userLoginBean = userLogin.get();
            LOGGER.info(userLoginBean.toString());
            return new User(
                    userLoginBean.getUsername(),
                    userLoginBean.getPassword(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList(
                            userLoginBean.getRole()
                    )
            );
        } else {
            LOGGER.error("User {} is not a registered user",username);
            throw new UsernameNotFoundException(
                    String.format("User %s is not a registered user",username)
            );
        }
    }
}
