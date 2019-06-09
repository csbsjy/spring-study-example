package com.module.api.service;

import com.module.common.domain.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    public void 회원등록_테스트(){
        Member member = Member.builder()
                .name("사용자2")
                .password("1234")
                .info("사용자2 입니다.")
                .build();

        Long id = memberService.signUp(member);

        assertThat(id, is(1L));

    }

}