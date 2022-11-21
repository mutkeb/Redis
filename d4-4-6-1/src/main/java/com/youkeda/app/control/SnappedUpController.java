package com.youkeda.app.control;

import com.youkeda.app.service.SnappedUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.CompletableFuture;

@Controller
public class SnappedUpController {
    @Autowired
    private SnappedUpService snappedUpService;

    /**
     * 抢购
     *
     * @return Result
     */
    @GetMapping("/snappedUp")
    @ResponseBody
    public boolean snappedUp(@RequestParam("id") Long id) {

        for (int i = 0; i < 10; i++) {
            CompletableFuture.supplyAsync(
                // 抢购
                () -> snappedUpService.snappedUp(id)
            ).thenAccept(r -> {
                if (r.getSuccess()) {
                    System.out.println("抢购成功。");
                } else {
                    System.out.println("抢购失败，" + r.getMessage());
                }

            });
        }

        return true;
    }
}
