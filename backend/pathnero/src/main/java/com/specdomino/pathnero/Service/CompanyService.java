package com.specdomino.pathnero.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Service;

import com.specdomino.pathnero.Entites.Company;
import com.specdomino.pathnero.Repositories.CompanyRepository;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    /**
     * Retrieves a list of all companies.
     * @return a list of Company objects
     */
    @QueryMapping
    public List<Company> companies() {
        List<Company> companies = companyRepository.findAll();
        return companies;
    }

        /**
     * Creates a new company with the specified name and description.
     *
     * @param name the name to set for the new company
     * @param description the description to set for the new company
     * @return the saved Company object
     */
    @MutationMapping
    public Company createCompany(@Argument String name,@Argument String description) {
        Company company = new Company();
        company.setName(name);
        company.setDescription(description);
        return companyRepository.save(company);
    }

    /**
     * Updates an existing company's details with the provided information.
     *
     * @param id the ID of the company to update
     * @param name the new name to set for the company
     * @param description the new description to set for the company
     * @return the updated Company object
     * @throws RuntimeException if the company is not found with the given id
     */
    @MutationMapping
    public Company updateCompany(@Argument Long id,@Argument String name,@Argument String description) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
        company.setName(name);
        company.setDescription(description);
        return companyRepository.save(company);
    }

    /**
     * Delete a company by ID.
     * @param companyId The ID of the company to delete.
     * @return true if the company was deleted, false otherwise.
     */
    @MutationMapping
    public Boolean deleteCompany(@Argument("id") Long companyId) {
        if(companyRepository.existsById(companyId)) {
            companyRepository.deleteById(companyId);
            return true;
        }
        return false;
    }
    
}
