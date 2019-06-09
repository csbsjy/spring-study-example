package com.module.web;

import com.module.common.domain.Member;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
public class MemberController {


    @GetMapping("/")
    public String memberInfo(){
        return new Member("1234","test","1234").toString();
    }

}
