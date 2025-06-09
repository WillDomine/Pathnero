package com.specdomino.pathnero.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Service;

import com.specdomino.pathnero.Entites.Job;
import com.specdomino.pathnero.Entites.JobApplication;
import com.specdomino.pathnero.Entites.UserProfile;
import com.specdomino.pathnero.Repositories.JobApplicationRepository;
import com.specdomino.pathnero.Repositories.JobRepository;
import com.specdomino.pathnero.Repositories.UserProfileRepository;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @QueryMapping
    public JobApplication jobApplication(@Argument Long id) {
        return jobApplicationRepository.findById(id).orElseThrow(() -> new RuntimeException("Job Application not found with id: " + id));
    }

    /**
     * Retrieves a list of JobApplication objects for the given jobId.
     * @param jobId the ID of the job posting
     * @return a list of JobApplication objects
     */
    @QueryMapping
    public List<JobApplication> getJobApplications(@Argument Long jobId) {
        List<JobApplication> jobApplications = jobApplicationRepository.findByJobId(jobId);
        return jobApplications;
    }

    @QueryMapping
    public List<JobApplication> getJobApplicationsByUser(@Argument Long userId) {
        List<JobApplication> jobApplications = jobApplicationRepository.findByUserId(userId);
        return jobApplications;
    }

    @QueryMapping
    public List<JobApplication> getAllJobApplications() {
        List<JobApplication> jobApplications = jobApplicationRepository.findAll();
        return jobApplications;
    }
    
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

}
