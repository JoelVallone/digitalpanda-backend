package org.digitalpanda.backend.application.northbound.ressource.greeting;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ui/greeting")
public class GreetingUiController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @CrossOrigin
    public GreetingDTO greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new GreetingDTO(counter.incrementAndGet(),
                            String.format(template, name));
    }
}
