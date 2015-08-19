package com.novadart.novabill.test.suite;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.service.mail.JavaMailService;
import com.novadart.novabill.service.mail.MailDispatcherService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:base-test-config.xml")
@ActiveProfiles("dev")
@DirtiesContext
@Transactional
public class BaseTest {

    @Autowired
    private MailDispatcherService mailDispatcherService;

    @Autowired
    private JavaMailService javaMailService;

    @Test
    public void alwaysTrueTest(){
        assertTrue(true);
    }

    @Before
    public void routeMailsToJavaMailService(){
        try {
            //mailgun mails are routed to java mail service
            TestUtils.setPrivateField(MailDispatcherService.class, mailDispatcherService, "mailGunSender", javaMailService);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
