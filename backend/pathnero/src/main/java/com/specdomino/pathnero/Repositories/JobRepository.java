package com.specdomino.pathnero.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.specdomino.pathnero.Entites.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
}

