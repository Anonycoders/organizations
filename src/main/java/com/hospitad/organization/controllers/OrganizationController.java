package com.hospitad.organization.controllers;

import com.hospitad.organization.error.OrganizationAlreadyExistException;
import com.hospitad.organization.error.OrganizationNotFoundException;
import com.hospitad.organization.models.Organization;
import com.hospitad.organization.models.User;
import com.hospitad.organization.repositories.OrganizationRepository;
import com.hospitad.organization.responses.GeneralResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationRepository organizationRepository;

    public OrganizationController(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @PostMapping
    public Organization setupOrganization(@AuthenticationPrincipal User user,
                                          @Validated @RequestBody Organization organization)
            throws OrganizationAlreadyExistException {
        if (organizationRepository.existsByName(organization.getName())) {
            throw new OrganizationAlreadyExistException("Organization already exists!");
        }
        organization.setOwner(user);
        return organizationRepository.save(organization);

    }

    @GetMapping
    public List<Organization> getOrganizationList(@AuthenticationPrincipal User user) {
        return organizationRepository.findAllByOwner(user);
    }

    @GetMapping("/{id}")
    public Organization getOrganization(@AuthenticationPrincipal User user,
                                        @PathVariable("id") Integer organizationId) throws OrganizationNotFoundException {

        Organization userOrganization = organizationRepository.findById(organizationId).orElseThrow(() ->
                new OrganizationNotFoundException(String.format("no organization found with id %d",
                        organizationId)));

        if (!user.getId().equals(userOrganization.getOwner().getId())){
            throw new OrganizationNotFoundException("no organization found with specified id!");
        }

        return userOrganization;
    }

    @DeleteMapping("/{id}")
    public GeneralResponse deleteOrganization(@AuthenticationPrincipal User user,
                                              @PathVariable("id") Integer organizationId) throws OrganizationNotFoundException {

        Organization userOrganization = organizationRepository.findById(organizationId).orElseThrow(() ->
                new OrganizationNotFoundException(String.format("no organization found with id %d",
                        organizationId)));

        if (!user.getId().equals(userOrganization.getOwner().getId())){
            throw new OrganizationNotFoundException("no organization found with specified id!");
        }

        organizationRepository.deleteById(organizationId);
        return new GeneralResponse("organization deleted successfully.");
    }

    @PutMapping("/{id}")
    public Organization editOrganization(@AuthenticationPrincipal User user,
                                         @PathVariable("id") Integer organizationId,
                                         @Validated @RequestBody Organization organization) throws OrganizationNotFoundException {

        Organization userOrganization = organizationRepository.findById(organizationId).orElseThrow(()
                -> new OrganizationNotFoundException("no organization found with specified id!"));

        if (!user.getId().equals(userOrganization.getOwner().getId())){
            throw new OrganizationNotFoundException("no organization found with specified id!");
        }

        organization.setId(userOrganization.getId());
        return organizationRepository.save(organization);

    }
}
