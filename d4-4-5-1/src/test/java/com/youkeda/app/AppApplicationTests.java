package com.youkeda.app;

import com.youkeda.app.control.SnappedUpController;
import com.youkeda.app.service.SnappedUpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SpringBootTest
public class AppApplicationTests {

    public static void error(String msg) {
        System.err.println("<YkdError>" + msg + "</YkdError>");
    }

    @Autowired
    private SnappedUpController snappedUpController;

    @Autowired
    private SnappedUpService snappedUpService;

    @Test
    public void runA() throws Exception {
        List<CompletableFuture> cfs = new ArrayList<>();
        //判断有没有按照要求实现代码
        for (int i = 0; i < 10; i++) {
            CompletableFuture<Void> cf = CompletableFuture.supplyAsync(
                // 抢购
                () -> snappedUpService.snappedUp(1L)).thenAccept(r -> {
                if (r.getSuccess()) {
                    System.out.println("抢购成功。");
                } else {
                    System.out.println("抢购失败，" + r.getMessage());
                }

            });

            cfs.add(cf);
        }

        try {
            // 等待所有的线程执行完毕
            CompletableFuture.allOf(cfs.toArray(new CompletableFuture[] {})).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
