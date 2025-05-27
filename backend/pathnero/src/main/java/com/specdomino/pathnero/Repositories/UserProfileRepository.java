package com.specdomino.pathnero.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.specdomino.pathnero.Entites.UserProfile;


public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByEmail(String email);

}
