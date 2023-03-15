package com.staytuned.staytuned.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
public class TestController {

    @PostMapping("/v1/api/upload/{name}")
    public void addMedia(@PathVariable String name, @RequestParam("data") MultipartFile mediaFile) throws IOException {
        log.info(mediaFile.getName());
        log.info(name);
    }
    @PostMapping("/test")
    public void test() throws IOException {
        log.info("요청 입력");
    }

}
