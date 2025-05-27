package com.specdomino.pathnero;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.specdomino.pathnero.Entites.Company;
import com.specdomino.pathnero.Entites.Job;
import com.specdomino.pathnero.Entites.JobApplication;
import com.specdomino.pathnero.Entites.UserProfile;
import com.specdomino.pathnero.Repositories.CompanyRepository;
import com.specdomino.pathnero.Repositories.JobApplicationRepository;
import com.specdomino.pathnero.Repositories.JobRepository;
import com.specdomino.pathnero.Repositories.UserProfileRepository;


@Component
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
    @PreAuthorize("permitAll()")
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
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
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
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public Boolean deleteJob(@Argument("id") Long jobId) {
        if(jobRepository.existsById(jobId)) {
            jobRepository.deleteById(jobId);
            return true;
        }
        return false;
    }

    @MutationMapping
    public UserProfile registerUser(@Argument String name,@Argument String email,@Argument String password,@Argument String role) {
        if (userProfileRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email is already in use!");
        }

        UserProfile userProfile = new UserProfile();
        userProfile.setName(name);
        userProfile.setEmail(email);
        userProfile.setRole("USER"); // Default role
        userProfile.setPassword(new BCryptPasswordEncoder().encode(password)); // Hash password

        return userProfileRepository.save(userProfile);
    }

    @MutationMapping
    public String login(@Argument String email, @Argument String password) {
        UserProfile user = userProfileRepository.findByEmail(email).orElse(null);
        if (user != null && user.checkPassword(password)) {
            return new JwtUtil().generateToken(email, user.getRole());
        }
        return null;
    }

}

