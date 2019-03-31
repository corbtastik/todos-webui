package io.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@Controller
@SpringBootApplication
public class WebUI {

	public static void main(String[] args) {
		SpringApplication.run(WebUI.class, args);
	}

}

