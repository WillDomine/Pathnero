package com.specdomino.pathnero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.Optional;

import com.specdomino.pathnero.Entites.Company;
import com.specdomino.pathnero.Entites.Job;
import com.specdomino.pathnero.Entites.JobApplication;
import com.specdomino.pathnero.Entites.UserProfile;

import com.specdomino.pathnero.Repositories.*;;;

@Controller
public class JobPortalController {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final UserProfileRepository userProfileRepository;
    private final JobApplicationRepository jobApplicationRepository;

    @Autowired
    public JobPortalController(JobRepository jobRepository,CompanyRepository companyRepository,UserProfileRepository userProfileRepository,JobApplicationRepository jobApplicationRepository) 
    {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.userProfileRepository = userProfileRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    //Queries

    @QueryMapping
    public List<Job> jobs() {
        return jobRepository.findAll();
    }

    @QueryMapping
    public Job job(@Argument Long id) {
        Optional<Job> jobOpt = jobRepository.findById(id);
        return jobOpt.orElse(null);
    }

    @QueryMapping
    public UserProfile userProfile(@Argument Long userId) {
        Optional<UserProfile> userOpt = userProfileRepository.findById(userId);
        return userOpt.orElse(null);
    }

    //Mutations

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

    @MutationMapping
    public Company createCompany(@Argument String name,@Argument String description) {
        Company company = new Company();
        company.setName(name);
        company.setDescription(description);
        return companyRepository.save(company);
    }

    @MutationMapping
    public JobApplication applyForJob(@Argument Long userId,@Argument Long jobId) {
        UserProfile user = userProfileRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
        JobApplication application = new JobApplication();
        application.setUser(user);
        application.setJob(job);
        application.setStatus("Applied");
        return jobApplicationRepository.save(application);
    }

    @MutationMapping
    public String deleteJob(@Argument Long jobID) {
        Job job = jobRepository.findById(jobID).orElseThrow(() -> new RuntimeException("Job not found with id: " + jobID));
        jobRepository.delete(job);
        return "Job with ID " + jobID + " has been deleted.";
    }

}

