package com.youkeda.comment.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.youkeda.comment.config.RedisTemplateConfig;
import com.youkeda.comment.model.PersonalRecord;
import com.youkeda.comment.service.PersonalRecordService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/record")
public class PersonalRecordControl {

    private static final TypeReference<List<PersonalRecord>> t_pr = new TypeReference<List<PersonalRecord>>() {
    };

    @Autowired
    private ResourceLoader loader;

    @Autowired
    private PersonalRecordService personalRecordService;

    @Autowired
    private RedisTemplateConfig redisTemplateConfig;

    @PostConstruct
    public void init() {
        String content = getContent("classpath:datas/personal_record.json");
        List<PersonalRecord> prs = JSON.parseObject(content, t_pr);

        for (PersonalRecord prData : prs) {
            personalRecordService.save(prData);
        }
    }

    private String getContent(String name) {
        try {
            InputStream in = loader.getResource(name).getInputStream();
            return IOUtils.toString(in, "utf-8");
        } catch (IOException e) {
            return null;
        }
    }
}
