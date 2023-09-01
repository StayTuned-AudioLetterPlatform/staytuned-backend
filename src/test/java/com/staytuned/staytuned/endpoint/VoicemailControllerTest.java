package com.staytuned.staytuned.endpoint;

import com.staytuned.staytuned.domain.*;
import com.staytuned.staytuned.endpoint.voicemail.AES256;
import com.staytuned.staytuned.endpoint.voicemail.VoicemailRequestDto;
import com.staytuned.staytuned.endpoint.voicemail.VoicemailResponseDto;

import com.staytuned.staytuned.security.jwt.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.assertj.core.api.Assertions.assertThat;
//
//@WebMvcTest(controllers = VoicemailController.class
//        , excludeFilters = {
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class),
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthorizationArgumentResolver.class)
//})
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@WebMvcTest
public class VoicemailControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private VoiceMailRepository voiceMailRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AES256 aes256;

    User testUser;
    String jwt;

    @BeforeEach
    public void setup() {
         testUser = User.builder()
                .name("test")
                .email("test@test.com")
                .picture(null)
                .role(Role.USER)
                .build();

        userRepository.save(testUser);

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("email", testUser.getEmail());
        claims.put("name", testUser.getName());
        claims.put("code", testUser.getCode());

        jwt = jwtUtil.generateAccessToken(claims);

        voiceMailRepository.save(VoiceMailEntity.builder()
                .writer("test1")
                .targetUserFK(testUser)
                .fileUrl("http://example/url")
                .iconType("2")
                .build());

        voiceMailRepository.save(VoiceMailEntity.builder()
                .writer("test1")
                .targetUserFK(testUser)
                .fileUrl("http://example/url")
                .iconType("1")
                .build());
    }

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
        voiceMailRepository.deleteAll();
    }


    @Test
    @DisplayName("voice mail 편지를 남긴다")
    void  createVoicemail(){
        //give
        String writer = "currentTestUser";
        String iconType = "1";
        Long targetUserCd = 1L;
        String fileUrl = "http://example/url";

        VoicemailRequestDto requestDto = VoicemailRequestDto.builder()
                .writer(writer)
                .targetUserCd(targetUserCd)
                .fileUrl(fileUrl)
                .iconType(iconType)
                .build();

        //when
        String url = "http://localhost:" + port + "/api/v1/voicemail/save";
        ResponseEntity<Long> response = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<VoiceMailEntity> all = voiceMailRepository.findAll();
        assertThat(all.get(2).getWriter()).isEqualTo(writer);
    }

    @DisplayName("User 가 본인의 VoicemailList 요청")
    @Test
    void getMyVoicemailList(){
        //when
        String url = "http://localhost:" + port + "/api/v1/voicemail/my";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt); // set the header value
        HttpEntity<String> request = new HttpEntity<String>(null, headers);
        ResponseEntity<VoicemailResponseDto> response = restTemplate.exchange(url, HttpMethod.GET, request, VoicemailResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(Objects.requireNonNull(response.getBody()).getVoicemailList().size())
                .isEqualTo(voiceMailRepository.findAll().size());
        assertTrue(response.getBody().getIsUser());
    }

    @DisplayName("만료된 jwt으로 VoicemailList 요청")
    @Test
    void getMyVoicemailListWithExpiredToken(){
        //given
        String currentJwt = "eyJ0eXBlIjoiSldUIiwiYWxnb3JpdGhtIjoiSFM1MTIiLCJhbGciOiJIUzUxMiJ9.eyJjb2RlIjoxLCJuYW1lIjoidGVzdCIsImVtYWlsIjoidGVzdEB0ZXN0LmNvbSIsImlhdCI6MTY5MzM4NDQ4MiwiZXhwIjoxNjkzMzg0NTQyfQ.8m54a6z46LrMSIyTZdbLTiT-V7uWaS3K8f2o0BxynjAPRjbD1tmaUZ38mijGC4pHTFMPkCj0s0p94gxTZPSq1Q";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ currentJwt); // set the header value
        HttpEntity<String> request = new HttpEntity<String>(null, headers);
        ResponseEntity<VoicemailResponseDto> response = restTemplate.exchange(
                "http://localhost:8080/api/v1/voicemail/getList", HttpMethod.GET, request, VoicemailResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("공유된 url로 user의 VoicemailList요청")
    @Test
    void getOtherVoicemailList(){
        String encodingString = aes256.AESEncrypt("1");;
        System.out.println(encodingString);
        //when
        String url = "http://localhost:" + port + "/api/v1/voicemail/user?userID=" + encodingString;
        ResponseEntity<VoicemailResponseDto> response = restTemplate.getForEntity(url, VoicemailResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(Objects.requireNonNull(response.getBody()).getVoicemailList().size())
                .isEqualTo(voiceMailRepository.findAll().size());
        assertFalse(response.getBody().getIsUser());
    }

    @DisplayName("Voicemail을 삭제 한다")
    @Test
    void deleteVoicemail(){

    }
}
