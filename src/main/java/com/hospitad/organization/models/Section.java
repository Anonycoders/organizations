package com.hospitad.organization.models;


import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Section {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @NotEmpty(message = "section name can not be empty!")
  @NotNull(message = "section name can not be null!")
  @Column(nullable = false)
  private String name;

  @OneToMany(targetEntity = Section.class, fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
  private Set<Section> sections = new HashSet<>();


    public Section(String name) {
        this.name = name;
    }

    public Section(@NotEmpty(message = "section name can not be empty!") @NotNull(message = "section name can not be null!") String name, Set<Section> sections) {
        this.name = name;
        this.sections = sections;
    }
}
