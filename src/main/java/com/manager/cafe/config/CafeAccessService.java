package com.manager.cafe.config;

import com.manager.cafe.entity.User;
import org.springframework.stereotype.Service;

@Service
public class CafeAccessService {

    public User getCafeOwner(User currentUser) {

        if (currentUser.getOwner() != null) {
            return currentUser.getOwner();
        }

        return currentUser;
    }
}
