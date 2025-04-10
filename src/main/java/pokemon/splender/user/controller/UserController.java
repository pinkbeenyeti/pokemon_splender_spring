package pokemon.splender.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pokemon.splender.response.CustomResponse;
import pokemon.splender.user.dto.UserNameEditRequest;
import pokemon.splender.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/name/edit")
    public ResponseEntity<?> editUserName(
        Authentication authentication,
        @RequestBody UserNameEditRequest request) {

        Long userId = (Long) authentication.getPrincipal(); // userId 그대로 꺼내기
        userService.updateUserName(userId, request.newName());

        return CustomResponse.ok();
    }
}
