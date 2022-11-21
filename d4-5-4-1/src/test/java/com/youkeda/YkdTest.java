package com.youkeda;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youkeda.comment.dao.UserDAO;
import com.youkeda.comment.model.PersonalRecord;
import com.youkeda.comment.model.Result;
import com.youkeda.comment.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class YkdTest {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RedisTemplate redisTemplate;

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private ObjectMapper mapper;

    public static void error(String msg) {
        System.err.println("<YkdError>" + msg + "</YkdError>");
    }

    @Test
    void contextLoads() throws Exception {
        Set<TypedTuple<PersonalRecord>> datas = redisTemplate.opsForZSet().rangeWithScores("integralRank", 0, -1);

        if (CollectionUtils.isEmpty(datas)) {
            error("查询 integralRank 数据失败。查询不到数据。");
            return;
        }

        Double score = 1233.0;
        Long id = 25L;
        boolean ok = false;
        for (TypedTuple<PersonalRecord> data : datas) {
            System.out.println(data.getValue().getId() + " - " + data.getScore());
            if (id.equals(data.getValue().getId()) && score.equals(data.getScore())){
                return;
            }
        }

        error("查询记录失败。必须包含 score = 1233.0 且 id = 25 的个人战绩数据记录。 ");
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
