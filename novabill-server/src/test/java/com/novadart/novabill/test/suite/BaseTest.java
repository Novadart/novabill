package com.novadart.novabill.test.suite;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:base-test-config.xml")
@ActiveProfiles("dev")
@Transactional
public class BaseTest {

    @Test
    public void alwaysTrueTest(){
        assertTrue(true);
    }

}
