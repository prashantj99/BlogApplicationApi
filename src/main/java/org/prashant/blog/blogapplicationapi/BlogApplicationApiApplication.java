package org.prashant.blog.blogapplicationapi;


import org.modelmapper.ModelMapper;
import org.prashant.blog.blogapplicationapi.entities.Role;
import org.prashant.blog.blogapplicationapi.repository.RoleRepository;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class BlogApplicationApiApplication implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;
    public static void main(String[] args) {
        SpringApplication.run(BlogApplicationApiApplication.class, args);
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... args) {
        try {
            Role admin_role=new Role(), user_role=new Role();
            admin_role.setId(AppConstant.ADMIN_USER);
            admin_role.setName("ADMIN");
            user_role.setId(AppConstant.NORMAL_USER);
            user_role.setName("NORMAL");
            this.roleRepository.saveAll(List.of(admin_role, user_role));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

