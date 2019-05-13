package ch.heigvd.pro.client.file;

import ch.heigvd.pro.client.structure.Safe;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ServerDriver implements IStorePasswordDriver {

    public ServerDriver(){
    }


    @Override
    public Safe loadSafe() {

        return null;
    }

    @Override
    public void saveSafe() {

    }

    /**
     *
     * @param username username
     * @param password password
     * @return return the User connected
     * @throws Exception return exception if the username and password is not good
     */
    public boolean login(String username, String password) throws Exception {

        HttpPost loginrequest = new HttpPost("http://127.0.0.1:8080/login");
        StringEntity informationToSend = new StringEntity("{\"username\": \"" + username + "\",\"password\": \"" + password + "\" }");
        JSONObject loginStatus = POSTrequest(informationToSend, loginrequest);

        if(loginStatus.get("errorCode").equals(0)){
            return true;
        }else {
            throw new LoginException(loginStatus.get("message").toString());
        }
    }

    /**
     * Create a new user in server
     * @param username username
     * @param email email address
     * @param password password
     * @return return the json get server value
     * @throws Exception
     */
    public JSONObject createUser(String username, String email, String password) throws Exception {
        HttpPost createUserrequest = new HttpPost("http://127.0.0.1:8080/user");
        StringEntity informationToSend = new StringEntity("{\"username\": \"" + username + "\",\"email\": \"" + email + "\",\"password\": \"" + password + "\" }");
        JSONObject createUserStatus = POSTrequest(informationToSend, createUserrequest);
        return createUserStatus;
    }

    /**
     * POST Request
     * @param informationToSend information to send to server
     * @param request request http to server
     * @return return the json from server
     * @throws Exception
     * Source: https://stackoverflow.com/questions/5769717/how-can-i-get-an-http-response-body-as-a-string-in-java
     *         https://stackoverflow.com/questions/7181534/http-post-using-json-in-java
     */
    private JSONObject POSTrequest(StringEntity informationToSend, HttpPost request) throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            request.addHeader("content-type", "text/plain");
            request.setEntity(informationToSend);

            // Send Post and Get http response
            HttpResponse loginAnswer = httpClient.execute(request);
            HttpEntity test1 = loginAnswer.getEntity();

            String answerJSONString = EntityUtils.toString(test1, "UTF-8");
            JSONObject answerJSON = new JSONObject(answerJSONString);

            return answerJSON;

        }catch (UnsupportedEncodingException e) {
            throw new UnsupportedEncodingException("Unsupported Encoding error");
        } catch (ClientProtocolException e) {
            throw new ClientProtocolException("Client protocole error");
        } catch (IOException e) {
            throw new IOException("There is a connexion error. Please check you're internet");
        }
    }
}
