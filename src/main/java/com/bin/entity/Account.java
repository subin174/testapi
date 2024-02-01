package com.bin.entity;

import com.bin.entity.enums.AccountStatus;
import com.bin.entity.enums.IdentityType;
import com.bin.entity.req.AccReq;
import com.bin.entity.req.AccountDto;
import com.bin.entity.resp.AccountResp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "nick_name")
    private String nickName;
    private String email;
    private String avatar;
    private String phone;
    private LocalDate date;
    @Column
    private String username;
    @Column
    @JsonIgnore
    private String password;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @Column(name = "identity_type")
    @Enumerated(EnumType.STRING)
    private IdentityType identityType;
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "roles_user", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonManagedReference
    private Set<Role> role;
    private LocalDate currentDate = LocalDate.now();
    private Integer age;
    private String pass;
//            = calculateAge(date, currentDate);

    public Account(AccountDto dto){
        BeanUtils.copyProperties(dto, this);
    }
    public Account(Long id){
        this.id = id;
    }
    public void updateAccount(AccountResp accountResp){
        this.nickName =accountResp.getNickName();
        this.firstName=accountResp.getFirstName();
        this.lastName=accountResp.getLastName();
        this.phone=accountResp.getPhone();
        this.avatar=accountResp.getAvatar();
        this.email=accountResp.getEmail();
        this.date=accountResp.getDate();
    }
    public Account(AccReq dto){
        BeanUtils.copyProperties(dto, this);
    }
    public long calculateAge(
            LocalDate date,
            LocalDate currentDate) {
        return Period.between(date, currentDate).getYears();
    }

}
