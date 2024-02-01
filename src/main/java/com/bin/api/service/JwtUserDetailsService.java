package com.bin.api.service;
import com.bin.entity.Account;
import com.bin.entity.Role;
import com.bin.entity.UserPrin;
import com.bin.entity.enums.AccountStatus;
import com.bin.entity.req.AccReq;
import com.bin.repository.AccountRepository;
import com.bin.repository.AuthenRepository;
import com.bin.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private AuthenRepository repository;
    @Autowired
    private PasswordEncoder bcryptEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserPrin loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = repository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
//        account.setAge(Period.between(account.getDate(), account.getCurrentDate()).getYears());
        List<GrantedAuthority> authorities = account.getRole().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return new UserPrin( account);
    }

    public Account save(AccReq accReq) throws Exception {
        Account newAccount = new Account(accReq);
        long count = repository.countByUsername( accReq.getUsername());
        if (count >0){
            throw new Exception("account.already.exist");
        }
        newAccount.setStatus(AccountStatus.ACTIVE);
        newAccount.setPassword(bcryptEncoder.encode(accReq.getPassword()));
        newAccount.setPass(accReq.getPassword());
        newAccount.setAge(Period.between(accReq.getDate(), newAccount.getCurrentDate()).getYears());
        Optional<Role> role  = roleRepository.findByName("USER");
        Set<Role> roles = new HashSet<>();
        role.isPresent();
        roles.add(role.get());
        newAccount.setRole(roles);
        newAccount.setAvatar("https://i.imgur.com/6WCf7zr.jpg");
        return repository.save(newAccount);
    }

    public static void main(String[] args) {
        Random rand = new Random();
        HashSet<Integer> set = new HashSet<>();

        while (set.size() < 6) {
            int randomNumber = rand.nextInt(55);
            set.add(randomNumber);
        }

        System.out.println("Random vietlott: " + set);
    }
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }
}
