package com.edu.tistory.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.edu.tistory.SpringFoApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = SpringFoApplication.class)
@RunWith(SpringRunner.class)
@WebAppConfiguration
@Slf4j
public class LoginControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mock;

    @Before
    public void beforeTest() {
        mock = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void test() throws Exception {
        this.mock.perform(get("/login/manager")
                .param("userId", "ccc")
        )
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

}
