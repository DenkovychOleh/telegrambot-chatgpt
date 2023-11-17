package telegrambotchatgpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import telegrambotchatgpt.entities.AppUser;
import telegrambotchatgpt.service.repositories.AppUserService;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserService appUserService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserService.findByUsername(username);
        return new User(appUser.getUsername(), appUser.getPassword(), appUser.getAuthorities());
    }
}