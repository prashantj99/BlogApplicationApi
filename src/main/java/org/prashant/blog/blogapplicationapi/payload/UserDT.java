package org.prashant.blog.blogapplicationapi.payload;

import org.prashant.blog.blogapplicationapi.entities.Account;
import org.prashant.blog.blogapplicationapi.entities.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record UserDT(
        Long userId,
        String name,
        String email,
        String about,
        String profileImg,
        List<Account> accounts,
        Set<RoleDT> roles
) {
   public UserDT(User user){
       this(user.getUserId(),
               user.getName(),
               user.getEmail(),
               user.getAbout(),
               user.getProfileImg(),
               user.getAccounts(),
               user.getRoles().stream().map(RoleDT::new).collect(Collectors.toSet())
               );
   }
}
