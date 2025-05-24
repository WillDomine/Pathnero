package com.specdomino.pathnero.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.specdomino.pathnero.Entites.JobApplication;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
}

