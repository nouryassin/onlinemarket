package com.example.demo.repositories;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.Test;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class, WebApplicationContext.class})
//@ContextConfiguration(locations = {"classpath:testContext.xml", "classpath:exampleApplicationContext-web.xml"})
@WebAppConfiguration
public class test3 {
 
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
 
    @Before
    public void setUp() {
        
 
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void testing() {
    	try {
			mockMvc.perform(get("/editOffer")).andExpect(view().name("editOffer"));
			
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}

