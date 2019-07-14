package com.webpack.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/api")
    public ResponseEntity<String> isOk(){
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @DeleteMapping("/api")
    public ResponseEntity<String> isBad(){
        return new ResponseEntity<>("Fail", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
