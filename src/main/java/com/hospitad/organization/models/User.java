package com.hospitad.organization.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Entity
@ToString(exclude = {"password", "organizations"})
@EqualsAndHashCode(exclude = {"password", "organizations"})
@NoArgsConstructor
@Data
public class User {
    public User(@NotNull(message = "username can not be null")
                @NotEmpty(message = "username can not be empty") String username,
                @NotNull(message = "email can not be null")
                @NotEmpty(message = "email can not be empty")
                @Email(message = "provided email address is not valid") String email,
                @NotNull(message = "password can not be null")
                @NotEmpty(message = "password can not be empty") String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true, nullable = false)
    @NotNull(message = "username can not be null")
    @NotEmpty(message = "username can not be empty")
    private String username;

    @Column(unique = true, nullable = false)
    @NotNull(message = "email can not be null")
    @NotEmpty(message = "email can not be empty")
    @Email(message = "provided email address is not valid")
    private String email;

    @Column(nullable = false)
    @NotNull(message = "password can not be null")
    @NotEmpty(message = "password can not be empty")
    private String password;


    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Organization> organizations;
}
