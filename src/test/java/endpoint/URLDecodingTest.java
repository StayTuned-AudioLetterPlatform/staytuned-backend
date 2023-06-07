package endpoint;

import com.staytuned.staytuned.StayTunedApplication;
import com.staytuned.staytuned.domain.VoiceMailRepository;
import com.staytuned.staytuned.endpoint.voicemail.UserStringDecoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = StayTunedApplication.class)
public class URLDecodingTest {

    @Autowired
    private UserStringDecoder userStringDecoder;

    @Test
    public void URLDecoding() throws Exception {
        String str =  userStringDecoder.decoder("%2BUhbIQOK4JuVOugdE8Ganw%3D%3D");
        System.out.println(">>>>>>>>>>>> decoding:  "+str);

        // then
        assertThat(str).contains("1");

    }

}
