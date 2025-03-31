package pokemon.splender.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pokemon.splender.response.CustomResponse;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    @GetMapping("/authenticated")
    public ResponseEntity<?> getUserAuthenticated() {
        return CustomResponse.ok();
    }
}
