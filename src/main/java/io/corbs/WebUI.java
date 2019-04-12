package io.corbs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SpringBootApplication
public class WebUI {

    public static void main(String[] args) {
		SpringApplication.run(WebUI.class, args);
	}

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("userId", "corbs");
        model.addAttribute("clientName", "corbs");
        model.addAttribute("userName", "corbs");
        return "index";
    }
}



