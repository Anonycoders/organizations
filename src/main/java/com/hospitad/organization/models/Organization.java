package com.hospitad.organization.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Organization {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty(message = "organization name can not be empty!")
    @NotNull(message = "organization name can not be null!")
    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Section> sections = new HashSet<>();

    public Organization(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    public Organization(@NotEmpty(message = "organization name can not be empty!") @NotNull(message = "organization name can not be null!") String name, User owner, Set<Section> sections) {
        this.name = name;
        this.owner = owner;
        this.sections = sections;
    }
}
