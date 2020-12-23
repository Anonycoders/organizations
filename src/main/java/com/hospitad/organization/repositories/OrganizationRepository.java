package com.hospitad.organization.repositories;

import com.hospitad.organization.models.Organization;
import com.hospitad.organization.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
    Optional<Organization> findByName(String name);
    boolean existsByName(String name);

    List<Organization> findAllByOwner(User user);
}
