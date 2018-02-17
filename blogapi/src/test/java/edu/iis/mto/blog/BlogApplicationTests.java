package edu.iis.mto.blog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan("edu.iis.mto.blog.domain")
public class BlogApplicationTests {

	@Test
	public void contextLoads() {
	}

}
