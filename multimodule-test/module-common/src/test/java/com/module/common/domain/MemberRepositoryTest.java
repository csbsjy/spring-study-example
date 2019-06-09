package com.module.common.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 사용자_저장_테스트(){
        Member member = Member.builder()
                .name("사용자1")
                .password("1234")
                .info("사용자1입니다")
                .build();

        assertThat(memberRepository.save(member).getName(), is("사용자1"));
    }


}