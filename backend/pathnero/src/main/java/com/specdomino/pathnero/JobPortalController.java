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
     * Retrieves a UserProfile object for the given userId.
     * @param userId the ID of the user profile
     * @return the UserProfile object, or null if not found
     */
    @QueryMapping
    public UserProfile userProfile(@Argument Long userId) {
        Optional<UserProfile> userOpt = userProfileRepository.findById(userId);
        return userOpt.orElse(null);
    }

    /**
     * Retrieves a list of JobApplication objects for the given jobId.
     * @param jobId the ID of the job posting
     * @return a list of JobApplication objects
     */
    @QueryMapping
    public List<JobApplication> jobApplications(@Argument Long jobId) {
        List<JobApplication> jobApplications = jobApplicationRepository.findByJobId(jobId);
        return jobApplications;
    }

    /**
     * Retrieves a list of all companies.
     * @return a list of Company objects
     */
    @QueryMapping
    public List<Company> companies() {
        List<Company> companies = companyRepository.findAll();
        return companies;
    }

    //Mutations

    // Job related mutations
    
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

    //Job Application related mutations

    /**
     * Apply for a job posting.
     * @param userId the ID of the user applying for the job
     * @param jobId the ID of the job posting to apply for
     * @return the saved JobApplication object
     * @throws RuntimeException if the user or job posting is not found
     */
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

    /**
     * Updates the status of a job application.
     *
     * @param applicationId the ID of the job application to update
     * @param status the new status to set for the job application
     * @return the updated JobApplication object
     * @throws RuntimeException if the job application is not found with the given id
     */
    @MutationMapping
    public JobApplication updateJobApplicationStatus(@Argument Long applicationId, @Argument String status) {
        JobApplication application = jobApplicationRepository.findById(applicationId).orElseThrow(() -> new RuntimeException("Job Application not found with id: " + applicationId));
        application.setStatus(status);
        return jobApplicationRepository.save(application);
    }

    /**
     * Deletes a job application by its ID.
     *
     * @param applicationId the ID of the job application to delete
     * @return true if the job application was successfully deleted, false otherwise
     */
    @MutationMapping
    public Boolean deleteJobApplication(@Argument Long applicationId) {
        if(jobApplicationRepository.existsById(applicationId)) {
            jobApplicationRepository.deleteById(applicationId);
            return true;
        }
        return false;
    }

    // Company related mutations

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

    // User related mutations

    /**
     * Registers a new user profile with the provided details.
     * 
     * @param name the name to set for the user
     * @param email the email to set for the user
     * @param password the password to set for the user
     * @return the saved UserProfile object
     * @throws IllegalArgumentException if the email is already in use
     */
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

    /**
     * Updates the user profile with the provided details.
     *
     * @param email the email of the user to update
     * @param name the new name to set for the user
     * @param password the new password to set for the user
     * @param resumeUrl the new resume URL to set for the user
     * @return the updated UserProfile object
     * @throws RuntimeException if the user is not found with the given email
     */
    @MutationMapping
    public UserProfile updateUser(@Argument String email, @Argument String name, @Argument String password, @Argument String resumeUrl) {
        UserProfile userProfile = userProfileRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        userProfile.setName(name);
        userProfile.setPassword(password); //Hash password
        userProfile.setResumeUrl(resumeUrl);
        return userProfileRepository.save(userProfile);
    }

    /**
     * Deletes a user profile by email.
     * @param email the email of the user to delete
     * @return true if the user was deleted, false otherwise
     */
    @MutationMapping
    public Boolean deleteUser(@Argument String email) {
        Optional<UserProfile> userOpt = userProfileRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            userProfileRepository.delete(userOpt.get());
            return true;
        }
        return false;
    }

}

