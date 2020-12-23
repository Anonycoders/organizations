package com.hospitad.organization.controllers;


import com.hospitad.organization.models.Organization;
import com.hospitad.organization.models.Section;
import com.hospitad.organization.models.User;
import com.hospitad.organization.repositories.OrganizationRepository;
import com.hospitad.organization.repositories.UserRepository;
import com.hospitad.organization.responses.GeneralResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrganizationControllersTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OrganizationRepository organizationRepository;

    private final String username = "user";
    private final String email = "email@gmail.com";
    private final String password = "password";
    private String token;

    @BeforeEach
    public void init() {
        userRepository.deleteAll();
        organizationRepository.deleteAll();
        registerUser();
        token = generateToken();
    }

    @Test
    public void editNonExistingOrganization() {
        initOrganization();

        Organization altOrganization = initAlterOrganization();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Organization> requestEntity = new HttpEntity<>(altOrganization, headers);

        ResponseEntity<Organization> responseEntity = restTemplate.exchange(String.format("/organizations/%d", 9999999),
                HttpMethod.PUT, requestEntity, Organization.class);

        Assertions.assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void editExistingOrganization() {
        initOrganization();

        Organization altOrganization = initAlterOrganization();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Organization> requestEntity = new HttpEntity<>(altOrganization, headers);

        ResponseEntity<Organization> responseEntity = restTemplate.exchange(String.format("/organizations/%d",
                organizationRepository.findByName("Fandogh").get().getId()),
                HttpMethod.PUT, requestEntity, Organization.class);

        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void deleteNonExistingOrganization() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Organization> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<GeneralResponse> responseEntity = restTemplate.exchange(String.format("/organizations/%d", 9999999),
                HttpMethod.DELETE, requestEntity, GeneralResponse.class);

        Assertions.assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void deleteExistingOrganizationWithId() {
        initOrganization();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<GeneralResponse> responseEntity = restTemplate.exchange(String.format("/organizations/%d",
                organizationRepository.findByName("Fandogh").get().getId()),
                HttpMethod.DELETE, requestEntity, GeneralResponse.class);

        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());

    }

    @Test
    public void getOrganizationWithId() {
        initOrganization();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Organization> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Organization> responseEntity = restTemplate.exchange(String.format("/organizations/%d",
                organizationRepository.findByName("Fandogh").get().getId()),
                HttpMethod.GET,
                requestEntity,
                Organization.class);

        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());

    }

    @Test
    public void getOrganizationWithFakeId() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Organization> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Organization> responseEntity = restTemplate.exchange(String.format("/organizations/%d",
                99999999),
                HttpMethod.GET,
                requestEntity,
                Organization.class);

        Assertions.assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void getOrganization_completeDiagram() {
        initOrganization();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Organization> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Organization[]> responseEntity = restTemplate.exchange("/organizations", HttpMethod.GET, requestEntity, Organization[].class);

        final var existingOrgs = responseEntity.getBody();
        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        assertThat(existingOrgs.length).isEqualTo(1);
        final var fandoghOrg = existingOrgs[0];
        assertThat(fandoghOrg.getName()).isEqualTo("Fandogh");
    }

    @Test
    public void postOrganization_succeedToCreateOrganization() {
        Organization organization = new Organization();
        organization.setName("Fandogh");
        Set<Section> sections = new LinkedHashSet<>();
        sections.add(new Section("Management"));
        sections.add(new Section("Design"));
        sections.add(new Section("Develop", Set.of(new Section("Software"), new Section("Hardware"))));
        organization.setSections(sections);


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Organization> requestEntity = new HttpEntity<>(organization, headers);

        ResponseEntity<Object> responseEntity = restTemplate.postForEntity("/organizations",
                requestEntity, Object.class);

        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void postOrganization_createOrganizationWithoutName() {
        Organization organization = new Organization();
        Set<Section> sections = new LinkedHashSet<>();
        sections.add(new Section("Management"));
        sections.add(new Section("Design"));
        sections.add(new Section("Develop", Set.of(new Section("Software"), new Section("Hardware"))));
        organization.setSections(sections);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Organization> requestEntity = new HttpEntity<>(organization, headers);

        ResponseEntity<Object> responseEntity = restTemplate.postForEntity("/organizations",
                requestEntity, Object.class);

        Assertions.assertEquals(400, responseEntity.getStatusCodeValue());
    }

    private Organization initAlterOrganization() {
        Organization organization = new Organization("Fandogh", userRepository.findByUsername(username).get());
        Set<Section> sections = new LinkedHashSet<>();
        sections.add(new Section("Management"));
        sections.add(new Section("Design", Set.of(new Section("Web"), new Section("Ads"))));
        sections.add(new Section("Develop", Set.of(new Section("Software",
                        Set.of(new Section("Frontend"), new Section("Backend"))),
                new Section("Hardware"))));
        organization.setSections(sections);
        return organization;
    }

    private void initOrganization() {
        Organization organization = new Organization("Fandogh", userRepository.findByUsername(username).get());
        Set<Section> sections = new LinkedHashSet<>();
        sections.add(new Section("Management"));
        sections.add(new Section("Design"));
        sections.add(new Section("Develop", Set.of(new Section("Software"), new Section("Hardware"))));
        organization.setSections(sections);
        organizationRepository.save(organization);

    }

    private void registerUser() {
        User user = new User(username, email, password);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);
        restTemplate.postForEntity("/users/register", requestEntity, User.class);
    }

    private String generateToken() {
        final Map<String, String> tokenMap = restTemplate.postForObject("/tokens", Map.of("username", username, "password", password), Map.class);
        return tokenMap.get("token");
    }
}
