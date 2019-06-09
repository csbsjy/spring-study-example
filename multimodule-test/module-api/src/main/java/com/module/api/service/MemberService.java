package com.module.api.service;


import com.module.common.domain.Member;
import com.module.common.domain.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberService {

    private MemberRepository memberRepository;

    public Long signUp(Member member){
        return memberRepository.save(member).getId();
    }

}
