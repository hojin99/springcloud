package com.inno.sc.ecuser.controller;

import com.inno.sc.ecuser.dto.UserDto;
import com.inno.sc.ecuser.jpa.UserEntity;
import com.inno.sc.ecuser.service.UserService;
import com.inno.sc.ecuser.vo.Greeting;
import com.inno.sc.ecuser.vo.RequestLogin;
import com.inno.sc.ecuser.vo.RequestUser;
import com.inno.sc.ecuser.vo.ResponseUser;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/")
public class UserServiceController {

    private Environment env;
    private UserService userService;
    private Greeting greeting;

    @Value("${server.port}")
    private String serverPort;

    public UserServiceController(Environment env, UserService userService, Greeting greeting) {
        this.env = env;
        this.userService = userService;
        this.greeting = greeting;
    }

    @GetMapping("/health_check")
    public String status() {

        return String.format("It's working in User Service"
                + ", " + env.getProperty("local.server.port")
                + ", " + env.getProperty("server.port")
                + ", " + env.getProperty("token.secret")
                + ", " + env.getProperty("token.expiration_time")
        );
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
    public ResponseEntity<List<ResponseUser>> getUsers() {
        Iterable<UserEntity> userList = userService.getUserByAll();

        List<ResponseUser> result = new ArrayList<>();
        userList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseUser.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result); // 200
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {

        UserDto userDto = userService.getUserByUserId(userId);
        ResponseUser returnValue = new ModelMapper().map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

}
