package ch.heigvd.pro.client.file;

import ch.heigvd.pro.client.structure.Entry;
import ch.heigvd.pro.client.structure.Folder;
import ch.heigvd.pro.client.structure.Safe;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

public class ServerDriver implements IStorePasswordDriver {

    private List<Folder> folderList;
    private String token;
    private int idUser;

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
            this.token = (String)loginStatus.get("token");
            DecodedJWT jwt = JWT.decode(this.token);
            this.idUser = jwt.getClaim("id").asInt();
            addEntry(1, -7,"facebook".toCharArray(), "Stefan".toCharArray(), "www.facebook.com".toCharArray(), "12345".toCharArray(), " dskléda".toCharArray(), new Date());
            getUserData();
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
     * Create folder that will contains passswords
     * @param folderName folder name
     * @return return true if it's created
     * @throws Exception
     */
    public boolean createFolder(String folderName) throws Exception {
        HttpPost createUserrequest = new HttpPost("http://127.0.0.1:8080/folder");
        StringEntity informationToSend = new StringEntity("{\"userId\": \"" + idUser + "\",\"name\": \"" + folderName + "\",\"passwords\": [{}] }");
        createUserrequest.addHeader("token", this.token);
        JSONObject createUserStatus = POSTrequest(informationToSend, createUserrequest);
       /* if(createUserStatus.get("errorCode").equals(0)){
            return true;
        } else{
            return false;
        }*/
       System.out.println(createUserStatus);
       return true;
    }

    // A Voir avec jerome
    public boolean addEntry(int idFolder, int id, char[] entryname, char[] username, char[] target, char[] clearPassword, char[] notes, Date registerDate) throws Exception {
        HttpPost createUserrequest = new HttpPost("http://127.0.0.1:8080/folder/" + idFolder + "/password");
        Entry newEntry = new Entry(id,  entryname,  username, target, clearPassword,  notes,  registerDate);
        //newEntry.encryptEntry();
        StringEntity informationToSend = new StringEntity(newEntry.JSONentry());
        createUserrequest.addHeader("token", this.token);
        JSONObject createUserStatus = POSTrequest(informationToSend, createUserrequest);

        System.out.println(createUserStatus);
        return true;
    }

    /**
     * It will get all user data
     * @return return the json data
     * source: https://www.testingexcellence.com/how-to-parse-json-in-java/
     */
    private void getUserData(){
        String result = "";
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet("http://127.0.0.1:8080/user/" + idUser + "/folders");
            httpget.addHeader("token", this.token);
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                result = EntityUtils.toString(response.getEntity());
                System.out.println("TEST" + result);
                JSONObject answerJSON = new JSONObject(result);
                JSONArray folders = answerJSON.getJSONArray("folders");
                System.out.println(folders);

                for(int i =0 ; i < folders.length(); ++i){
                    System.out.println(folders.getJSONObject(i).get("name"));
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {

        }
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
