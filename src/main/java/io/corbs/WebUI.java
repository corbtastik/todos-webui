package io.corbs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class Todo implements Serializable {
    private Integer id;
    private String title;
    private Boolean completed = Boolean.FALSE;
}

@Controller
@SpringBootApplication
public class WebUI {

    public static void main(String[] args) {
		SpringApplication.run(WebUI.class, args);
	}

    @GetMapping("/")
    public String index(Model model) {
//        LOG.info("principalName: " + authorizedClient.getPrincipalName());
//        LOG.info("tokenType: " + authorizedClient.getAccessToken().getTokenType().getValue());
//        LOG.info("tokenValue: " + authorizedClient.getAccessToken().getTokenValue());
//        LOG.info("refreshToken" + authorizedClient.getRefreshToken().getTokenValue());
        model.addAttribute("userId", "fakeUser");
        model.addAttribute("clientName", "fakeClientRegistration");
        model.addAttribute("token", "fakeToken");
        model.addAttribute("userName", "fakeUsername");
        return "index";
    }

}



