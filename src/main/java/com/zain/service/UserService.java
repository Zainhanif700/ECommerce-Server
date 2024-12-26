package com.zain.service;

import com.zain.exception.UserException;
import com.zain.model.User;
import org.springframework.stereotype.Service;

public interface UserService {

    User findUserById(Long Id) throws UserException;

    User findUserProfileByJwt(String jwt) throws UserException;

}
