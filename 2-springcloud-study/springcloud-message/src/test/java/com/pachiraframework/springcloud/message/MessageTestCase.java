package com.pachiraframework.springcloud.message;

import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lizzy.springcloud.message.MessageApplication;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(classes=MessageApplication.class)
public class MessageTestCase {
	
}
