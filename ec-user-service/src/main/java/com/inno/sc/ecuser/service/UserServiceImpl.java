package com.inno.sc.ecuser.service;

import com.inno.sc.ecuser.client.OrderServiceClient;
import com.inno.sc.ecuser.dto.UserDto;
import com.inno.sc.ecuser.jpa.UserEntity;
import com.inno.sc.ecuser.jpa.UserRepository;
import com.inno.sc.ecuser.vo.ResponseOrder;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;
    Environment env;
    RestTemplate restTemplate;
    OrderServiceClient orderServiceClient;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, Environment env, RestTemplate restTemplate, OrderServiceClient orderServiceClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
        this.restTemplate = restTemplate;
        this.orderServiceClient = orderServiceClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        // 사용자가 DB에 존재하지 않을 경우
        if(userEntity == null)
            throw new UsernameNotFoundException(username);

        // 사용자가 DB에 존재할 때
        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true,
                true,
                true,
                true,
                new ArrayList<>());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        // id 생성
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));
        userRepository.save(userEntity);

        UserDto retUserdto = mapper.map(userEntity, UserDto.class);

        return retUserdto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null)
            throw new UsernameNotFoundException("User not found");

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        // RestTemplate을 통한 서비스 호출
//        String orderUrl = String.format(env.getProperty("order_service.url"), userId);
//        ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//                new ParameterizedTypeReference<List<ResponseOrder>>() {
//        });
//        List<ResponseOrder> orderList = orderListResponse.getBody();

        // Feign Client를 통한 서비스 호출 (try .. catch 대신 ErrorDecorder로 일괄 에러처리 가능)
//        try {
            List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);
            userDto.setOrders(orderList);
//        } catch (FeignException ex) {
//            log.error(ex.toString());
//        }

        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    // UserDetailsService에 정의 된 메소드로 스프링 시큐리티 인증 사용 시 필요

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null)
            throw new UsernameNotFoundException(email);

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        return userDto;
    }
}
