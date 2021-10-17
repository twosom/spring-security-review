package com.icloud.form;

import com.icloud.common.SecurityLogger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SampleService {

    public void dashboard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        System.err.println("=========================");
        System.err.println(authentication.hashCode());
        System.err.println(user.getUsername());
    }

    @Async
    public void asyncService() {
        SecurityLogger.log("Async service");
        System.out.println("Async service is called.");
    }
}
