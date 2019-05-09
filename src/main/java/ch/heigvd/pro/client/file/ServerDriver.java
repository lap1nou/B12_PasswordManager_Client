package ch.heigvd.pro.client.file;

import ch.heigvd.pro.client.structure.Safe;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.google.gson.Gson;
import jdk.nashorn.api.scripting.JSObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ServerDriver implements IStorePasswordDriver {
    private String token;
    private int idUser;

    public ServerDriver(String username, String password) {
        login(username, password);
    }

    @Override
    public Safe loadSafe(File file) {

        return null;
    }

    @Override
    public void saveSafe(Safe safe, File file) {

    }

    // Source: https://stackoverflow.com/questions/5769717/how-can-i-get-an-http-response-body-as-a-string-in-java and https://stackoverflow.com/questions/7181534/http-post-using-json-in-java
    private boolean login(String username, String password) {
        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost loginRequest = new HttpPost("http://127.0.0.1:8080/login");
        try {
            StringEntity loginJson = new StringEntity("{username: \"" + username + "\",password: \"" + password + "\" }");
            loginRequest.addHeader("content-type", "text/plain");
            loginRequest.setEntity(loginJson);

            HttpResponse loginAnswer = httpClient.execute(loginRequest);
            HttpEntity test1 = loginAnswer.getEntity();

            String answerJSONString = EntityUtils.toString(test1, "UTF-8");
            JSONObject answerJSON = new JSONObject(answerJSONString);

            this.token = answerJSON.get("token").toString();

            if (answerJSON.get("errorCode").equals("0")) {
                DecodedJWT jwt = JWT.decode(this.token);
                this.idUser = jwt.getClaim("id").asInt();

                return true;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
