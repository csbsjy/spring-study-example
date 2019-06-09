package com.example.demo;

import com.example.demo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserInfoById(@PathVariable("id") String id){
        User user = new User();
        user.setId(id);
        user.setName("User1");
        user.setAge(25);
        user.setInfo("This is Test User!!!");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/user", produces = "application/json")
    public ResponseEntity<User> createUserByUserModel(User user){
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
