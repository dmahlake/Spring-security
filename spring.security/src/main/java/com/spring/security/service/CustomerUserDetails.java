package com.spring.security.service;

import com.spring.security.dto.LoginDto;
import com.spring.security.dto.SignUpDto;
import com.spring.security.persistence.model.Customer;
import com.spring.security.persistence.model.Roles;
import com.spring.security.persistence.repo.CustomerRepo;
import com.spring.security.persistence.repo.RolesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.singleton;

@Service
public class CustomerUserDetails implements UserDetailsService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private RolesRepo rolesRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmailAddress) throws UsernameNotFoundException {

        Customer user = customerRepo.findByUsernameOrEmailAddress(usernameOrEmailAddress, usernameOrEmailAddress)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email:" + usernameOrEmailAddress));
        return new org.springframework.security.core.userdetails.User(user.getEmailAddress(),
                user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Set<Roles> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public String login(LoginDto loginDto)
    {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmailAddress(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "Signed in successfull";
    }

    public Customer registerUser(SignUpDto signUpDto)
    {
        Customer customer = new Customer();
        customer.setEmailAddress(signUpDto.getEmailAddress());
        customer.setUsername(signUpDto.getUsername());
        customer.setName(signUpDto.getName());
        customer.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        Roles roles = rolesRepo.findByName("ROLE_ADMIN").get();
        customer.setRoles(singleton(roles));

        return customerRepo.save(customer);
    }
}
