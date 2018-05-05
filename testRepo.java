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
public class testRepo {
 
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
			mockMvc.perform(get("/showToEdit")).andExpect(view().name("showToEdit"));
			
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}

//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.testng.AssertJUnit.assertNotNull;
//
//import org.junit.runner.RunWith;
//import org.testng.annotations.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.testng.annotations.Test;
//
//import com.example.demo.Onlinemarket2Application;
//import com.example.demo.entities.OnlineStore;
//import com.example.demo.repositories.onlineStoreRepository;
//
//@RunWith(SpringRunner.class)
//// specifies the Spring configuration to load for this test fixture
//@ContextConfiguration(classes= {onlineStoreRepository.class,OnlineStore.class})
//@DataJpaTest
//public class testRepo {
//
//    // this instance will be dependency injected by type
//    @Autowired
//	private onlineStoreRepository titleRepository;
//
//    @Autowired
//    public void setTitleRepository(onlineStoreRepository titleRepository) {
//        this.titleRepository = titleRepository;
//    }
//
//    @Test
//    public void find() throws Exception {
//
//        MockMvc mockMvc = null;
//        mockMvc.perform(post("/showToEdit"));
//        
//        
////        OnlineStore title = titleRepository.findById(11);
////        assertNotNull("XC");
//    }
//}