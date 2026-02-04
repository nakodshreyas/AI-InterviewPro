package com.InterviewPro;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InterviewProApplicationTests {

	@Test
	void contextLoads() {
	}

    void test(ChatClient chatClient) {
        String prompt = "Hello %s".formatted("World");

        chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
