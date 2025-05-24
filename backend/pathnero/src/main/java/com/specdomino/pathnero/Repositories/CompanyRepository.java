package com.specdomino.pathnero.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.specdomino.pathnero.Entites.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
