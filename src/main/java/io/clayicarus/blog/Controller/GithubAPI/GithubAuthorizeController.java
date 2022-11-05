package io.clayicarus.blog.Controller.GithubAPI;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class GithubAuthorizeController {

    @GetMapping("/callback")    // step2: redirect back to /callback
    String callback(@RequestParam(name = "code") String code,
                    @RequestParam(name = "state") String state)
    {
        // TODO: if state not match
        // POST https://github.com/login/oauth/access_token to get token
        TokenGetter tg = new TokenGetter();
        tg.setCode(code);
        tg.setState(state);
        tg.setRedirect_uri(redirect_uri);
        tg.setClient_id(client_id);
        tg.setClient_secret(client_secret);
        String token = getAccessToken(tg); // POST
        getUser(token); // step3
        return "index";
    }

    private String getAccessToken(TokenGetter getter)
    {
        MediaType json
                = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String sg = JSON.toJSONString(getter);
        RequestBody body = RequestBody.create(json, sg);
        System.out.println(sg);
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .removeHeader("Accept")
                .addHeader("Accept", "application/json")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if(response.isSuccessful()) {
                String s = response.body().string();
                System.out.println(s);
                TokenPackage tp = JSON.parseObject(s, TokenPackage.class);
                return tp.getAccess_token();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private GithubUser getUser(String token)
    {
        // use token to GET user info
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder()
                .url("https://api.github.com/user")
                .addHeader("Authorization", "Bearer " + token)
                .build();
        try {
            Response res = client.newCall(req).execute();
            if(res.isSuccessful()) {
                String s = res.body().string();
                System.out.println(s);
                GithubUser user = JSON.parseObject(s, GithubUser.class);
                System.out.println(user.getLogin());
                return user;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Value("${github.client_id}")
    private String client_id;
    @Value("${github.client_secret}")
    private String client_secret;
    @Value("${github.redirect_uri}")
    private String redirect_uri;
}
