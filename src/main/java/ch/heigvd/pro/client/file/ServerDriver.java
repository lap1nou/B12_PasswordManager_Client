package ch.heigvd.pro.client.file;

import ch.heigvd.pro.client.Utils;
import ch.heigvd.pro.client.structure.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import javax.security.auth.login.LoginException;

import java.io.IOException;
import java.nio.CharBuffer;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.List;

public class ServerDriver implements IStorePasswordDriver {
    private String token;
    private int idUser;
    private Safe safe;
    private User user;

    // TODO: Create .properties file
    //private static String SERVER_ADDRESS = "https://impass.bigcube.ch";
    private static String SERVER_ADDRESS = "http://127.0.0.1:8080";

    /**
     * Check every 15 minutes to change the token
     */
    Thread renewToken = new Thread() {
        public void run() {
            try {
                while (true) {
                    System.out.println(token);
                    Thread.sleep(15 * 60 * 1000);

                    HttpPost tokenrequest = new HttpPost(SERVER_ADDRESS + "/renew");
                    tokenrequest.addHeader("token", token);
                    JSONObject tokenStatus = POSTrequest(null, tokenrequest);

                    System.out.println(tokenStatus);
                    if (tokenStatus.get("errorCode").equals(0)) {
                        token = (String) tokenStatus.get("token");
                        System.out.println(token);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public ServerDriver() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
    }

    @Override
    public Safe loadSafe() {
        return null;
    }

    @Override
    public void saveSafe() {
    }

    public Safe getSafe() {
        return safe;
    }

    public void setSafe(Safe safe) {
        this.safe = safe;
    }

    /**
     * @param username username
     * @param password password
     * @return return the User connected
     * @throws Exception return exception if the username and password is not good
     */
    public Safe login(char[] username, char[] password) throws Exception {

        // Request for login
        HttpPost loginrequest = new HttpPost(SERVER_ADDRESS + "/login");
        StringEntity informationToSend = new StringEntity("{\"username\": \"" + CharBuffer.wrap(username) + "\",\"password\": \"" + CharBuffer.wrap(password) + "\" }");
        JSONObject loginStatus = POSTrequest(informationToSend, loginrequest);

        if (loginStatus.get("errorCode").equals(0)) {
            this.token = (String) loginStatus.get("token");
            renewToken.start();
            DecodedJWT jwt = JWT.decode(this.token);
            this.idUser = jwt.getClaim("id").asInt();
            Safe safe = getUserData(password);
            this.user = getUserInformation();
            return safe;
        } else {
            throw new LoginException(loginStatus.get("message").toString());
        }
    }

    /**
     * Create a new user in server
     *
     * @param username username
     * @param email    email address
     * @param password password
     * @return return the json get server value
     * @throws Exception
     */
    public void createUser(char[] username, char[] email, char[] password) throws Exception {
        HttpPost createUserrequest = new HttpPost(SERVER_ADDRESS + "/user");
        StringEntity informationToSend = new StringEntity("{\"username\": \"" + CharBuffer.wrap(username) + "\",\"email\": \"" + CharBuffer.wrap(email) + "\",\"password\": \"" + CharBuffer.wrap(password) + "\" }");
        JSONObject createUserStatus = POSTrequest(informationToSend, createUserrequest);

        if (!createUserStatus.get("errorCode").equals(0)) {
            throw new Exception(createUserStatus.get("message").toString());
        }
    }

    public User getUserInformation() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(SERVER_ADDRESS + "/user/" + idUser);
        httpget.addHeader("token", this.token);
        CloseableHttpResponse response = httpclient.execute(httpget);

        String result = EntityUtils.toString(response.getEntity());
        JSONObject answerJSON = new JSONObject(result);
        System.out.println(answerJSON);
        JSONObject JSONtest = (JSONObject)answerJSON.get("user");

        System.out.println("groups: " + JSONtest.getJSONArray("groups"));
        List<Group> groups = new ArrayList<Group>();
        for(Object test : JSONtest.getJSONArray("groups")){
            System.out.println(((JSONObject)test).get("name"));
            groups.add(new Group((String)((JSONObject)test).get("name"), (String)((JSONObject)test).get("right")));
        }

        User userProfile = new User((Integer)JSONtest.get("id"), (String)JSONtest.get("username"), (String)JSONtest.get("email"), groups);

        return userProfile;
    }

    /**
     * Create folder that will contains passswords
     *
     * @param folderName folder name
     * @throws Exception
     */
    public void createFolder(String folderName) throws Exception {
        // Create in the Safe
        safe.getFolderList().add(new Folder(folderName, new ArrayList<Entry>()));

        // Create on the Server
        HttpPost createFolderrequest = new HttpPost(SERVER_ADDRESS + "/folder");
        StringEntity informationToSend = new StringEntity("{\"userId\": \"" + idUser + "\",\"name\": \"" + CharBuffer.wrap(folderName).toString() + "\",\"passwords\": [{}] }");
        createFolderrequest.addHeader("token", this.token);
        JSONObject createFolderStatus = POSTrequest(informationToSend, createFolderrequest);

        System.out.println(createFolderStatus);
        if (!createFolderStatus.get("errorCode").equals(0)) {
            throw new Exception(createFolderStatus.get("message").toString());
        }
    }

    /**
     * Create an entry in the server
     *
     * @param newEntry             Entry created
     * @param selectedFolderNumber the selected folder number
     * @throws Exception
     */
    public void addEntry(Entry newEntry, int selectedFolderNumber) throws Exception {
        int idFolder = this.safe.getFolderList().get(selectedFolderNumber).getId();

        // Add it to the Safe
        this.safe.getFolderList().get(selectedFolderNumber).addEntry(newEntry);

        // Add it to the Server
        HttpPost addEntryrequest = new HttpPost(SERVER_ADDRESS + "/folder/" + idFolder + "/password");
        newEntry.encryptEntry(safe.getSafePassword());

        StringEntity informationToSend = new StringEntity(newEntry.JSONentry());
        addEntryrequest.addHeader("token", this.token);
        JSONObject addEntryStatus = POSTrequest(informationToSend, addEntryrequest);
        newEntry.decryptEntry(safe.getSafePassword());

        System.out.println(addEntryStatus);

        if (!addEntryStatus.get("errorCode").equals(0)) {
            throw new Exception(addEntryStatus.get("message").toString());
        }
    }

    /**
     * Edit an entry in the server
     *
     * @param actualEntry The actual entry
     * @param editedEntry The entry to edit
     * @throws Exception
     */
    public void editEntry(Entry actualEntry, Entry editedEntry) throws Exception {
        // Update in the Safe
        actualEntry.setUsername(editedEntry.getUsername());
        actualEntry.setEntryName(editedEntry.getEntryName());
        actualEntry.setClearPassword(editedEntry.getClearPassword());
        actualEntry.setTarget(editedEntry.getTarget());
        actualEntry.setNotes(editedEntry.getNotes());
        actualEntry.setIcon(editedEntry.getIcon());

        // Update on Server
        HttpPut editEntryrequest = new HttpPut(SERVER_ADDRESS + "/password/" + actualEntry.getId());
        actualEntry.encryptEntry(safe.getSafePassword());

        StringEntity informationToSend = new StringEntity(actualEntry.JSONentry());

        // TODO: Do I have to put token here ?
        //editEntryrequest.addHeader("token", this.token);

        JSONObject addEntryStatus = POSTrequest(informationToSend, editEntryrequest);
        actualEntry.decryptEntry(safe.getSafePassword());

        if (!addEntryStatus.get("errorCode").equals(0)) {
            throw new Exception(addEntryStatus.get("message").toString());
        }

        System.out.println(addEntryStatus);
    }

    /**
     * Delete an entry
     *
     * @param selectedFolderNumber the index of the selected entry
     * @param indexOfEntryToRemove the index of the entry to remove
     * @throws Exception
     */
    public void deleteEntry(int selectedFolderNumber, int indexOfEntryToRemove) throws Exception {
        int idPassword = safe.getFolderList().get(selectedFolderNumber).getEntrylist().get(indexOfEntryToRemove).getId();

        // Delete from Safe
        safe.getFolderList().get(selectedFolderNumber).removeEntry(indexOfEntryToRemove);

        // Delete on Server
        HttpDelete deleteEntryRequete = new HttpDelete(SERVER_ADDRESS + "/password/" + idPassword);
        deleteEntryRequete.addHeader("token", this.token);

        deleteEntryRequete.addHeader("content-type", "text/plain");

        // Send DELETE Request
        HttpClient httpClient = HttpClients.custom().build();
        HttpResponse loginAnswer = httpClient.execute(deleteEntryRequete);
        HttpEntity httpEntitiy = loginAnswer.getEntity();
        JSONObject answerJSON = new JSONObject(EntityUtils.toString(httpEntitiy));

        System.out.println(answerJSON);

        if (!answerJSON.get("errorCode").equals(0)) {
            throw new Exception(answerJSON.get("message").toString());
        }

    }

    /**
     * It will get all user data
     *
     * @return return the json data
     * source: https://www.testingexcellence.com/how-to-parse-json-in-java/
     */
    private Safe getUserData(char[] password) throws Exception {
        try {
            String result = "";
            Safe safe = new Safe();
            byte[] iv = new byte[16];

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(SERVER_ADDRESS + "/user/" + idUser + "/folders");
            httpget.addHeader("token", this.token);
            CloseableHttpResponse response = httpclient.execute(httpget);

            result = EntityUtils.toString(response.getEntity());
            JSONObject answerJSON = new JSONObject(result);
            JSONArray folders = answerJSON.getJSONArray("folders");

            safe.setSafePassword(password);

            for (int i = 0; i < folders.length(); ++i) {
                List<Entry> folderEntry = new ArrayList<Entry>();
                for (int j = 0; j < folders.getJSONObject(i).getJSONArray("passwords").length(); ++j) {
                    folderEntry.add(new Entry((folders.getJSONObject(i).getJSONArray("passwords").getJSONObject(j).get("note")).toString().toCharArray(),
                            folders.getJSONObject(i).getJSONArray("passwords").getJSONObject(j).get("password").toString().toCharArray(),
                            folders.getJSONObject(i).getJSONArray("passwords").getJSONObject(j).get("salt").toString().toCharArray(),
                            folders.getJSONObject(i).getJSONArray("passwords").getJSONObject(j).get("icon").toString(),
                            (Integer) folders.getJSONObject(i).getJSONArray("passwords").getJSONObject(j).get("id"),
                            folders.getJSONObject(i).getJSONArray("passwords").getJSONObject(j).get("title").toString().toCharArray(),
                            Utils.JSONArrayTobyte(folders.getJSONObject(i).getJSONArray("passwords").getJSONObject(j).getJSONArray("iv")),
                            folders.getJSONObject(i).getJSONArray("passwords").getJSONObject(j).get("target").toString().toCharArray(),
                            folders.getJSONObject(i).getJSONArray("passwords").getJSONObject(j).get("username").toString().toCharArray()
                    ));
                }
                safe.addFolder(new Folder((String) folders.getJSONObject(i).get("name"), folderEntry, (Integer) folders.getJSONObject(i).get("id")));
            }

            return safe;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * @param groupName
     * @throws Exception
     */
    public void createGroupe(char[] groupName) throws Exception {
        // Add group on the server
        HttpPost addEntryrequest = new HttpPost(SERVER_ADDRESS + "/group");

        StringEntity informationToSend = new StringEntity("{ \"name\": \"" + CharBuffer.wrap(groupName).toString() + "\" }");
        addEntryrequest.addHeader("token", this.token);
        JSONObject addEntryStatus = POSTrequest(informationToSend, addEntryrequest);

        // Add group locally
        this.user.addGroup(new Group(CharBuffer.wrap(groupName).toString(), "ADMIN"));

        System.out.println(addEntryStatus);

        if (!addEntryStatus.get("errorCode").equals(0)) {
            throw new Exception(addEntryStatus.get("message").toString());
        }
    }

    /**
     * POST Request
     *
     * @param informationToSend information to send to server
     * @param request           request http to server
     * @return return the json from server
     * @throws Exception Source: https://stackoverflow.com/questions/5769717/how-can-i-get-an-http-response-body-as-a-string-in-java
     *                   https://stackoverflow.com/questions/7181534/http-post-using-json-in-java
     */
    private JSONObject POSTrequest(StringEntity informationToSend, HttpEntityEnclosingRequestBase request) throws Exception {

        HostnameVerifier allowAllHosts = new NoopHostnameVerifier();

        SSLContext sslContext = SSLContextBuilder
                .create()
                .loadTrustMaterial(new TrustSelfSignedStrategy())
                .build();

        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);

        HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(connectionFactory).setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).build();


        try {
            request.addHeader("content-type", "application/json");
            request.setEntity(informationToSend);

            // Send Post and Get http response
            HttpResponse loginAnswer = httpClient.execute(request);
            HttpEntity test1 = loginAnswer.getEntity();

            String answerJSONString = EntityUtils.toString(test1, "UTF-8");
            JSONObject answerJSON = new JSONObject(answerJSONString); // <-- ici l'erreur se produit

            return answerJSON;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
