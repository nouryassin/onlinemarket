package com.example.demo.controller;

import static org.testng.AssertJUnit.assertNotNull;

import org.junit.runner.RunWith;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.annotations.Test;

import com.example.demo.Onlinemarket2Application;
import com.example.demo.entities.OnlineStore;
import com.example.demo.repositories.onlineStoreRepository;

@RunWith(SpringRunner.class)
// specifies the Spring configuration to load for this test fixture
@ContextConfiguration(classes= {Onlinemarket2Application.class})
@DataJpaTest
public class HibernateTitleRepositoryTests {

    // this instance will be dependency injected by type
    @Autowired
	private onlineStoreRepository titleRepository;

    @Autowired
    public void setTitleRepository(onlineStoreRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    @Test
    public void find() {
        OnlineStore store = titleRepository.findById(11);
        assertNotNull(store);
    }
}