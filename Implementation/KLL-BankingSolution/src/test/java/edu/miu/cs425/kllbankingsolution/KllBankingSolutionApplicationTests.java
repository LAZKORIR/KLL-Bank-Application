package edu.miu.cs425.kllbankingsolution;

import edu.miu.cs425.kllbankingsolution.utils.CustomUserDetailsService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KllBankingSolutionApplicationTests {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
//	@Test
//	void contextLoads() {
//	}
@PostConstruct
public void testCustomUserDetailsService() {
	customUserDetailsService.loadUserByUsername("luwam");
}


}
