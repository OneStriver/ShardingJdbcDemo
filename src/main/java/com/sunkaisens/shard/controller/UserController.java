package com.sunkaisens.shard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sunkaisens.shard.entity.Role;
import com.sunkaisens.shard.entity.User;
import com.sunkaisens.shard.service.RoleService;
import com.sunkaisens.shard.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    //测试
    @RequestMapping(value="addUser")
    public String updateTransactional(@RequestParam(value = "id") Integer id,
                                      @RequestParam(value = "name") String name,
                                      @RequestParam(value = "address") String address) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setAddress(address);
        userService.saveUser(user);
        
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        roleService.saveRole(role);
        
        return "success";
    }
}
