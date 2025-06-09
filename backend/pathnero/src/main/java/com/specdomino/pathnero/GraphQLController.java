package com.specdomino.pathnero;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.specdomino.pathnero.Entites.Company;
import com.specdomino.pathnero.Entites.Job;
import com.specdomino.pathnero.Entites.JobApplication;
import com.specdomino.pathnero.Entites.UserProfile;
import com.specdomino.pathnero.Service.CompanyService;
import com.specdomino.pathnero.Service.JobApplicationService;
import com.specdomino.pathnero.Service.JobService;
import com.specdomino.pathnero.Service.UserProfileService;

@Controller
public class GraphQLController {

    private final CompanyService companyService;
    private final JobService jobService;
    private final JobApplicationService jobApplicationService;
    private final UserProfileService userProfileService;

    @Autowired
    public GraphQLController(
            CompanyService companyService,
            JobService jobService,
            JobApplicationService jobApplicationService,
            UserProfileService userProfileService
    ) {
        this.companyService = companyService;
        this.jobService = jobService;
        this.jobApplicationService = jobApplicationService;
        this.userProfileService = userProfileService;
    }

    // --- Job Queries & Mutations ---
    @QueryMapping
    public List<Job> jobs() {
        return jobService.jobs();
    }

    @QueryMapping
    public Job job(@Argument Long id) {
        return jobService.job(id);
    }

    @MutationMapping
    public Job createJob(@Argument String title, @Argument int salary, @Argument String description, @Argument String location, @Argument Long companyId) {
        return jobService.createJob(title, salary, description, location, companyId);
    }

    @MutationMapping
    public Job updateJob(@Argument Long id, @Argument String title, @Argument int salary, @Argument String description, @Argument String location) {
        return jobService.updateJob(id, title, salary, description, location);
    }

    @MutationMapping
    public Boolean deleteJob(@Argument("id") Long jobId) {
        return jobService.deleteJob(jobId);
    }

    // --- Company Queries & Mutations ---
    @QueryMapping
    public Company company(@Argument Long id) {
        return companyService.company(id);
    }

    @QueryMapping
    public List<Company> companies() {
        return companyService.companies();
    }

    @MutationMapping
    public Company createCompany(@Argument String name, @Argument String description) {
        return companyService.createCompany(name, description);
    }

    @MutationMapping
    public Company updateCompany(@Argument Long id, @Argument String name, @Argument String description) {
        return companyService.updateCompany(id, name, description);
    }

    @MutationMapping
    public Boolean deleteCompany(@Argument("id") Long companyId) {
        return companyService.deleteCompany(companyId);
    }

    // --- Job Application Queries & Mutations ---
    @QueryMapping
    public JobApplication jobApplication(@Argument Long id) {
        return jobApplicationService.jobApplication(id);
    }

    @QueryMapping
    public List<JobApplication> getJobApplications(@Argument Long jobId) {
        return jobApplicationService.getJobApplications(jobId);
    }

    @QueryMapping
    public List<JobApplication> getJobApplicationsByUser(@Argument Long userId) {
        return jobApplicationService.getJobApplicationsByUser(userId);
    }

    @QueryMapping
    public List<JobApplication> getAllJobApplications() {
        return jobApplicationService.getAllJobApplications();
    }

    @MutationMapping
    public JobApplication applyForJob(@Argument Long userId, @Argument Long jobId) {
        return jobApplicationService.applyForJob(userId, jobId);
    }

    @MutationMapping
    public JobApplication updateJobApplicationStatus(@Argument Long applicationId, @Argument String status) {
        return jobApplicationService.updateJobApplicationStatus(applicationId, status);
    }

    @MutationMapping
    public Boolean deleteJobApplication(@Argument Long applicationId) {
        return jobApplicationService.deleteJobApplication(applicationId);
    }

    // --- User Profile Queries & Mutations ---
    @QueryMapping
    public UserProfile userProfile(@Argument String email) {
        return userProfileService.userProfile(email);
    }

    @QueryMapping
    public List<UserProfile> getAllUsers() {
        return userProfileService.getAllUsers();
    }

    @MutationMapping
    public UserProfile registerUser(@Argument String name, @Argument String email, @Argument String password) {
        return userProfileService.registerUser(name, email, password);
    }

    @MutationMapping
    public UserProfile updateUser(@Argument String email, @Argument String name, @Argument String password, @Argument String resumeUrl) {
        return userProfileService.updateUser(email, name, password, resumeUrl);
    }

    @MutationMapping
    public Boolean deleteUser(@Argument String email) {
        return userProfileService.deleteUser(email);
    }
}
