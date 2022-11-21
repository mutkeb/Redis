package com.youkeda.app.control;

import com.youkeda.app.model.Result;
import com.youkeda.app.service.SnappedUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
        List<CompletableFuture> cfs = new ArrayList<>();
        //  模拟多线程抢购，产生10个线程
        for (int i = 0; i < 10; i++){
            CompletableFuture<Void> cf = CompletableFuture.supplyAsync(() -> snappedUpService.snappedUp(id))
                    .thenAccept(booleanResult -> {
                        if (booleanResult.getSuccess()) {
                            System.out.println("抢购成功。");
                        } else {
                            System.out.println("抢购失败，" + booleanResult.getMessage());
                        }
                    });
            cfs.add(cf);
        }
        try {
            CompletableFuture.allOf(cfs.toArray(new CompletableFuture[]{})).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return true;
    }
}
