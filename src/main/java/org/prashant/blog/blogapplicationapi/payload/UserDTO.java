package org.prashant.blog.blogapplicationapi.payload;

import org.prashant.blog.blogapplicationapi.entities.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record UserDTO(
        Long userId,
        String name,
        String email,
        String about,
        String profileImg,
        List<AccountDTO> accounts,
        Set<RoleDTO> roles
) {
   public UserDTO(User user){
       this(user.getUserId(),
               user.getName(),
               user.getEmail(),
               user.getAbout(),
               user.getProfileImg(),
               user.getAccounts().stream().map((acc)-> new AccountDTO(acc.getAccountId(), acc.getLink(), acc.getPlatform())).toList(),
               user.getRoles().stream().map(RoleDTO::new).collect(Collectors.toSet())
               );
   }
}
