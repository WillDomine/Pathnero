package com.specdomino.pathnero.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Service;

import com.specdomino.pathnero.Entites.UserProfile;
import com.specdomino.pathnero.Repositories.UserProfileRepository;

@Service
public class UserProfileService {
    
    @Autowired
    private UserProfileRepository userProfileRepository;

    /**
     * Retrieves a UserProfile object for the given userId.
     * @param userId the ID of the user profile
     * @return the UserProfile object, or null if not found
     */
    @QueryMapping
    public UserProfile userProfile(@Argument String email) {
        Optional<UserProfile> userOpt = userProfileRepository.findByEmail(email);
        return userOpt.orElse(null);
    }

    @QueryMapping
    public List<UserProfile> getAllUsers() {
        return userProfileRepository.findAll();
    }

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
