package com.youkeda.app;

import com.youkeda.app.control.SnappedUpController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AppApplicationTests {

    public static void error(String msg) {
        System.err.println("<YkdError>" + msg + "</YkdError>");
    }

    @Autowired
    private SnappedUpController snappedUpController;

    @Test
    public void runA() {

    }

}
