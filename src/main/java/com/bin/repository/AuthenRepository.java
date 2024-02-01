package com.bin.repository;


import com.bin.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenRepository extends CrudRepository<Account,Integer> {
    Account findByUsername(String username);
    long countByUsername( String username);
}
