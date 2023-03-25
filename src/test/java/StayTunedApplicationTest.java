import com.staytuned.staytuned.StayTunedApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = StayTunedApplication.class)
public class StayTunedApplicationTest {
    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }
    @Test
    public void contextLoads() {
    }
}
