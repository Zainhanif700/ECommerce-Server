package com.zain.service;

import com.zain.exception.UserException;
import com.zain.model.User;
import com.zain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    private UserRepository userRepository;
    private JwtService jwtProvider;

    public UserServiceImplementation(UserRepository userRepository, JwtService jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public User findUserById(Long id) throws UserException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        throw new UserException("User not found with id: "+ id);
    }

    @Override
    public User findUserByUserName(String userName) throws UserException {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(userName));
        if (user.isPresent()) {
            return user.get();
        }
        throw new UserException("User not found with userName: "+ userName);
    }


    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        System.out.println("user");
        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw  new UserException("User not found with email: "+ email);
        }
        return user;
    }
}
