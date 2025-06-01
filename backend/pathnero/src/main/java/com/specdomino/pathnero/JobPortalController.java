package com.specdomino.pathnero;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.specdomino.pathnero.Entites.Company;
import com.specdomino.pathnero.Entites.Job;
import com.specdomino.pathnero.Entites.JobApplication;
import com.specdomino.pathnero.Entites.UserProfile;
import com.specdomino.pathnero.Repositories.CompanyRepository;
import com.specdomino.pathnero.Repositories.JobApplicationRepository;
import com.specdomino.pathnero.Repositories.JobRepository;
import com.specdomino.pathnero.Repositories.UserProfileRepository;


@Controller
public class JobPortalController {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final UserProfileRepository userProfileRepository;
    private final JobApplicationRepository jobApplicationRepository;

    @Autowired
    public JobPortalController(JobRepository jobRepository,CompanyRepository companyRepository,UserProfileRepository userProfileRepository,JobApplicationRepository jobApplicationRepository) 
    {
        System.out.println("JobPortalController created");
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.userProfileRepository = userProfileRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    //Queries

    @QueryMapping
    public List<Job> jobs() {
        List<Job> jobs = jobRepository.findAll();
        System.out.println("Jobs retrieved by repository: " + (jobs != null ? jobs.size() : "null"));
        return jobs;
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

    @QueryMapping
    public List<JobApplication> jobApplications(@Argument Long jobId) {
        List<JobApplication> jobApplications = jobApplicationRepository.findByJobId(jobId);
        return jobApplications;
    }

    @QueryMapping
    public List<Company> companies() {
        List<Company> companies = companyRepository.findAll();
        return companies;
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
    public Boolean deleteJob(@Argument("id") Long jobId) {
        if(jobRepository.existsById(jobId)) {
            jobRepository.deleteById(jobId);
            return true;
        }
        return false;
    }

    @MutationMapping
    public UserProfile registerUser(@Argument String name,@Argument String email,@Argument String password) {
        if (userProfileRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email is already in use!");
        }

        UserProfile userProfile = new UserProfile();
        userProfile.setName(name);
        userProfile.setEmail(email);
        userProfile.setRole("USER"); // Default role
        userProfile.setPassword(password); // Hash password

        return userProfileRepository.save(userProfile);
    }

}

