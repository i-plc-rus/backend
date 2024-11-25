package before;

import org.springframework.stereotype.Component;

// TODO заменить на Slf4j
@Component
public class Logger {

    public void log(String message) {
        System.out.println("LOG: " + message);
    }
}
