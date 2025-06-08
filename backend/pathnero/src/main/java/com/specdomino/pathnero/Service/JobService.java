package com.specdomino.pathnero.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Service;

import com.specdomino.pathnero.Entites.Company;
import com.specdomino.pathnero.Entites.Job;
import com.specdomino.pathnero.Repositories.CompanyRepository;
import com.specdomino.pathnero.Repositories.JobRepository;

@Service
public class JobService {
    
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

        /**
     * Retrieves a list of all jobs.
     * @return the list of Job objects
     */
    @QueryMapping
    public List<Job> jobs() {
        List<Job> jobs = jobRepository.findAll();
        return jobs;
    }

    /**
     * Retrieves a Job object for the given ID.
     * @param id the ID of the job
     * @return the Job object, or null if not found
     */
    @QueryMapping
    public Job job(@Argument Long id) {
        Optional<Job> jobOpt = jobRepository.findById(id);
        return jobOpt.orElse(null);
    }

       /**
     * Creates a new job posting with the specified details.
     *
     * @param title the title to set for the new job
     * @param salary the salary to set for the new job
     * @param description the description to set for the new job
     * @param location the location to set for the new job
     * @param companyId the ID of the company associated with the job
     * @return the saved Job object
     * @throws RuntimeException if the company is not found with the given id
     */
    @MutationMapping
    public Job createJob(@Argument String title,@Argument int salary, @Argument String description,@Argument String location,@Argument Long companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));
        Job job = new Job();
        job.setTitle(title);
        job.setSalary(salary);
        job.setDescription(description);
        job.setLocation(location);
        job.setCompany(company);
        return jobRepository.save(job);
    }

    /**
     * Updates an existing job's details with the provided information.
     *
     * @param id the ID of the job to update
     * @param title the new title to set for the job
     * @param salary the new salary to set for the job
     * @param description the new description to set for the job
     * @param location the new location to set for the job
     * @return the updated Job object
     * @throws RuntimeException if the job is not found with the given id
     */
    @MutationMapping
    public Job updateJob(@Argument Long id, @Argument String title, @Argument int salary, @Argument String description, @Argument String location) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
        job.setTitle(title);
        job.setSalary(salary);
        job.setDescription(description);
        job.setLocation(location);
        return jobRepository.save(job);
    }

    /**
     * Deletes a job by its ID.
     *
     * @param jobId The ID of the job to delete.
     * @return true if the job was successfully deleted, false otherwise.
     */
    @MutationMapping
    public Boolean deleteJob(@Argument("id") Long jobId) {
        if(jobRepository.existsById(jobId)) {
            jobRepository.deleteById(jobId);
            return true;
        }
        return false;
    }

}
