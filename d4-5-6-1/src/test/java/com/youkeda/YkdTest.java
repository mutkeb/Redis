package com.youkeda;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youkeda.comment.control.PersonalRecordControl;
import com.youkeda.comment.model.PersonalRecord;
import com.youkeda.comment.model.Result;
import com.youkeda.comment.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class YkdTest {

    @Autowired
    private PersonalRecordControl personalRecordControl;

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private ObjectMapper mapper;

    public static void error(String msg) {
        System.err.println("<YkdError>" + msg + "</YkdError>");
    }

    @Test
    void contextLoads() throws Exception {
        try {
            assertData();
        } catch (Exception e) {
            error("检查代码时出现错误。请仔细查看错误提示。");
            throw e;
        }
    }

    private void assertData() throws Exception {
        Result<List<PersonalRecord>> result = personalRecordControl.queryAllRank();
        Assert.notNull(result, "错误：queryAllRank() 返回 null ");

        List<PersonalRecord> datas = result.getData();
        Assert.notEmpty(datas, "错误：queryAllRank() 未返回数据。");
        Assert.isTrue(datas.size() > 2, "错误：queryAllRank() 返回的数据太少，需要初始化数据。");

        PersonalRecord p1 = datas.get(0);
        PersonalRecord p2 = datas.get(1);

        Assert.isTrue(p1.getPoints() > p2.getPoints(), "错误：queryAllRank() 返回的数据排序不正确，积分高的个人战绩在前。");

        Result<List<PersonalRecord>> result2 = personalRecordControl.queryLimitAllRank(10L);
        Assert.notNull(result2, "错误：queryLimitAllRank() 返回 null ");

        List<PersonalRecord> datas2 = result2.getData();
        Assert.notEmpty(datas2, "错误：queryLimitAllRank() 未返回数据。");
        Assert.isTrue(datas2.size() == 10, "错误：queryLimitAllRank(10L) 返回的数量错误，不是10条。");

        PersonalRecord p21 = datas2.get(0);
        PersonalRecord p22 = datas2.get(1);

        Assert.isTrue(p21.getPoints() > p22.getPoints(), "错误：queryLimitAllRank() 返回的数据排序不正确，积分高的个人战绩在前。");

    }

    private Result<User> httpPost(String url) throws URISyntaxException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://localhost:" + randomServerPort + "/" + url;
        URI uri = new URI(baseUrl);

        //        HttpHeaders headers = new HttpHeaders();
        //        headers.setContentType(MediaType.APPLICATION_JSON);
        //        HttpEntity<String> request = new HttpEntity<String>(mapper.writeValueAsString(param), headers);

        String value = restTemplate.exchange(uri, HttpMethod.POST, null, String.class).getBody();

        return mapper.readValue(value, new TypeReference<Result<User>>() {
        });
    }
}
