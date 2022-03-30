package com.inno.sc.ecuser.controller;

import com.inno.sc.ecuser.dto.UserDto;
import com.inno.sc.ecuser.service.UserService;
import com.inno.sc.ecuser.vo.Greeting;
import com.inno.sc.ecuser.vo.RequestUser;
import com.inno.sc.ecuser.vo.ResponseUser;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/")
public class UserServiceController {

    @Autowired
    UserService userService;

    @Autowired
    private Greeting greeting;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/health_check")
    public String status() {
        return "It's working in User Service";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

        ResponseUser reponseUser = mapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(reponseUser); // 201
    }

    @GetMapping("/users")
    public String getUsers() {

        return "";
    }




}
