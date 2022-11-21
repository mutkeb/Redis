package com.youkeda;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youkeda.comment.dao.UserDAO;
import com.youkeda.comment.dataobject.UserDO;
import com.youkeda.comment.model.Result;
import com.youkeda.comment.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class YkdTest {

    @Autowired
    private UserDAO userDAO;

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private ObjectMapper mapper;

    public static void error(String msg) {
        System.err.println("<YkdError>" + msg + "</YkdError>");
    }

    @Test
    void contextLoads() throws Exception {

        long l = System.currentTimeMillis();
        String userName = "t" + l;

        Result<User> result = httpPost("/api/user/reg?userName=" + userName + "&pwd=123");

        if (!result.isSuccess()) {
            error("注册 API 没有正确的处理返回值");
            return;
        }

        UserDO userDO = userDAO.findByUserName(userName);
        if (userDO == null) {
            error("用户没有成功的插入");
            return;
        }

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
