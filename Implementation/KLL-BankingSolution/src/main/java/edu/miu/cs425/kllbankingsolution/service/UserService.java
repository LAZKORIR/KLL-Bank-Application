package edu.miu.cs425.kllbankingsolution.service;

import edu.miu.cs425.kllbankingsolution.dto.Response;
import edu.miu.cs425.kllbankingsolution.dto.UserPojo;
import edu.miu.cs425.kllbankingsolution.entities.User;
import edu.miu.cs425.kllbankingsolution.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Response createUser(UserPojo userPojo){
        Response response = new Response();
        try {
            User user = new User();
            user.setUsername(userPojo.getUsername());
            user.setPassword(userPojo.getPassword());
            userRepository.save(user);

            response.setResponseCode("200");
            response.setResponseMessage("User Created");

        }catch(Exception e){
            e.printStackTrace();
            response.setResponseCode("500");
            response.setResponseMessage("Failed to create user");

        }
        return response;
    }

    public Response updateUser(UserPojo userPojo){
        Response response = new Response();

        User user = userRepository.findByUsername(userPojo.getUsername());
        try {
            //update only provided field in the userpojo
            user.setUsername(userPojo.getUsername());
            user.setPassword(userPojo.getPassword());
            userRepository.save(user);

            response.setResponseCode("200");
            response.setResponseMessage("User Updated");
        }catch(Exception e){
            e.printStackTrace();
            response.setResponseCode("500");
            response.setResponseMessage("Failed to update user");

        }
        return response;
    }
}
