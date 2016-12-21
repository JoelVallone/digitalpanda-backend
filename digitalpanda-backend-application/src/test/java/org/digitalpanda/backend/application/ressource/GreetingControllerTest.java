package org.digitalpanda.backend.application.ressource;

import org.digitalpanda.backend.data.Greeting;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GreetingControllerTest {
    private static final String  FIRST_NAME= "heinrich";
    private GreetingController greetingController;

    @Before
    public void init() {
        this.greetingController = new GreetingController();
    }

    @Test
    public void should_get_greeting_with_name() {
        Greeting greeting = this.greetingController.greeting(FIRST_NAME);
        assertTrue( "should contain the name in the response",
                    greeting.getContent().contains(FIRST_NAME));
    }
}
