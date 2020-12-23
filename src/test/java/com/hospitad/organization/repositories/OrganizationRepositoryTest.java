package com.hospitad.organization.repositories;

import com.hospitad.organization.models.Organization;
import com.hospitad.organization.models.Section;
import com.hospitad.organization.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrganizationRepositoryTest {

  @Autowired
  private OrganizationRepository repository;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  public void beforeAll() {
    userRepository.deleteAll();
  }

  @Test
  public void testAddOrg() {
    User user = new User("u", "email@yahoo.com", "password");
    user = userRepository.save(user);
    Organization org = new Organization("org1", user);
    Section section1 = new Section("s1");
    org.getSections().add(section1);
    Section section11 = new Section("s1-1");
    section1.getSections().add(section11);

    Section section2 = new Section("s2");
    org.getSections().add(section2);

    repository.save(org);
  }

  @Test
  public void testDeleteOrg() {
    User user = new User("u", "email@yahoo.com", "password");
    user = userRepository.save(user);
    Organization org = new Organization("org1", user);
    Section section1 = new Section("s1");
    org.getSections().add(section1);
    Section section11 = new Section("s1-1");
    section1.getSections().add(section11);

    Section section2 = new Section("s2");
    org.getSections().add(section2);

    repository.save(org);

    repository.delete(org);

    assertThat(repository.findByName(org.getName())).isEmpty();
  }

  @Test
  public void testUpdateOrg(){
    User user = new User("u", "email@yahoo.com", "password");
    user = userRepository.save(user);
    Organization org = new Organization("org1", user);
    Section section1 = new Section("s1");
    org.getSections().add(section1);
    Section section11 = new Section("s1-1");
    section1.getSections().add(section11);

    Section section2 = new Section("s2");
    org.getSections().add(section2);

    repository.save(org);

    final var savedOrg = repository.findByName("org1").get();
    savedOrg.getSections().clear();
    savedOrg.getSections().add(new Section(section2.getId(),section2.getName(), new HashSet<>()));
    repository.save(savedOrg);

    final var updatedOrg = repository.findByName("org1").get();
    assertThat(updatedOrg).isEqualTo(savedOrg);
  }
}