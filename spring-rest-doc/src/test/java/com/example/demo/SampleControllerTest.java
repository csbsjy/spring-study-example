package com.example.demo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class SampleControllerTest {

    /* snippets이 생성될 위치를 지정하는 부분으로 아무것도 지정하지 않을 시
   Maven의 경우, target/generated-snippets
   Gradle의 경우, build/generated-snippets
   에 생성된다.*/
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    private MockMvc mockMvc;


    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(document("{method-name}/{class-name}"))
                .build();
    }

    @Test
    public void getUserInfoById() throws Exception {
        this.mockMvc.perform(get("/user/user1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("index",
                        responseFields(
                                fieldWithPath("id").description("The Board's number"),
                                fieldWithPath("name").description("The Board's title"),
                                fieldWithPath("age").description("The Board's contents"),
                                fieldWithPath("info").description("The Board's writeName")
                        )
                ));

    }

    @Test
    public void createUserByUserModel() throws Exception {
        this.mockMvc.perform(post("/user")
                .param("id", "user1")
                .param("name", "username1")
                .param("age", "22")
                .param("info", "Is this Alright?"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("index", requestParameters(
                        parameterWithName("id").description("User's id"),
                        parameterWithName("name").description("User's name"),
                        parameterWithName("age").description("User's age"),
                        parameterWithName("info").description("User's info")),
                        responseFields(
                                fieldWithPath("id").description("The Board's number"),
                                fieldWithPath("name").description("The Board's title"),
                                fieldWithPath("age").description("The Board's contents"),
                                fieldWithPath("info").description("The Board's writeName")
                        )
                ));


    }
}




