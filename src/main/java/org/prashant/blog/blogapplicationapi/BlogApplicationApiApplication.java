package org.prashant.blog.blogapplicationapi;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.prashant.blog.blogapplicationapi.entities.Role;
import org.prashant.blog.blogapplicationapi.repository.RoleRepository;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class BlogApplicationApiApplication {

    private static final Logger logger = LoggerFactory.getLogger(BlogApplicationApiApplication.class);
    private final RoleRepository roleRepository;

    @Autowired
    public BlogApplicationApiApplication(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(BlogApplicationApiApplication.class, args);
    }

//    @Bean
//    public void initRoles() {
//        try {
//            roleRepository.saveAll(List.of(buildRoles()));
//            logger.info("Roles have been successfully saved.");
//        } catch (Exception e) {
//            logger.error("Error occurred while saving roles: ", e);
//        }
//    }
//
//    private Role[] buildRoles() {
//        Role adminRole = new Role();
//        adminRole.setId(AppConstant.ADMIN_USER);
//        adminRole.setName("ADMIN");
//
//        Role userRole = new Role();
//        userRole.setId(AppConstant.NORMAL_USER);
//        userRole.setName("NORMAL");
//
//        return new Role[]{adminRole, userRole};
//    }
}
