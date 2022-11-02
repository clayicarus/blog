package io.clayicarus.blog.Controller.GithubAPI;

import okhttp3.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class GithubAuthorizeController {

    @GetMapping("/callback")
    String callback(@RequestParam(name = "code") String code,
                    @RequestParam(name = "state") String state)
    {
        AccessToken at = new AccessToken();
        at.setCode(code);
        at.setState(state);
        at.setRedirect_url("http://localhost:8080/callback");
        at.setClient_id("cfa721171e57930434e9");
        at.setClient_secret("910611883bce0ae7f16d0e7349fe68541a25b8e7");

        return "index";
    }

    String getAccessToken()
    {
        MediaType JSON
                = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
            System.out.println(response.body().string());
        } catch(IOException e) {}
        return null;
    }


}
