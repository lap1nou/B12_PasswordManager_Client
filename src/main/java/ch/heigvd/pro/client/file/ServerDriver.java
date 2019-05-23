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

import java.nio.CharBuffer;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.List;

public class ServerDriver implements IStorePasswordDriver {
    private String token;
    private int idUser;
    private ArrayList<Safe> safe = new ArrayList<>();
    private User user;
    private HttpClient httpClient;

    //private static String SERVER_ADDRESS = "https://impass.bigcube.ch";
    private static String SERVER_ADDRESS = "http://127.0.0.1:8080";

    /**
     * Check every 15 minutes to change the token
     */
    Thread renewToken = new Thread() {
        public void run() {
            try {
                while (true) {
                    Thread.sleep(15 * 60 * 1000);
                    Thread.sleep(15 * 60 * 1000);

                    HttpPost tokenrequest = new HttpPost(SERVER_ADDRESS + "/renew");
                    tokenrequest.addHeader("token", token);
                    JSONObject tokenStatus = sendRequest(null, tokenrequest);

                    if (tokenStatus.get("errorCode").equals(0)) {
                        token = (String) tokenStatus.get("token");
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
        HostnameVerifier allowAllHosts = new NoopHostnameVerifier();
        SSLContext sslContext = SSLContextBuilder
                .create()
                .loadTrustMaterial(new TrustSelfSignedStrategy())
                .build();
        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);

        this.httpClient = HttpClients.custom().setSSLSocketFactory(connectionFactory).setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).build();
    }

    @Override
    public Safe loadSafe() {
        return null;
    }

    @Override
    public void saveSafe() {
    }

    @Override
    public Safe getSafe(int safeIndex) {
        return safe.get(safeIndex);
    }

    @Override
    public void addSafe(Safe safe) {
        this.safe.add(safe);
    }

    public int getSafeSize() {
        return safe.size();
    }

    public User getUser() {
        return user;
    }

    /**
     * @param username username
     * @param password password
     * @return return the main safe
     * @throws Exception return exception if the username and password is not good
     */
    @Override
    public Safe login(char[] username, char[] password) throws Exception {

        // Request for login
        HttpPost loginRequest = new HttpPost(SERVER_ADDRESS + "/login");
        StringEntity informationToSend = new StringEntity("{\"username\": \"" + CharBuffer.wrap(username) + "\",\"password\": \"" + CharBuffer.wrap(password) + "\" }");
        JSONObject loginStatus = sendRequest(informationToSend, loginRequest);

        if (loginStatus.get("errorCode").equals(0)) {
            this.token = (String) loginStatus.get("token");
            renewToken.start();
            DecodedJWT jwt = JWT.decode(this.token);
            this.idUser = jwt.getClaim("id").asInt();
            this.user = getUserInformation();
            Safe safe = getUserData(password);

            return safe;
        }

        return null;
    }

    /**
     * Create a new user
     *
     * @param username username
     * @param email    email address
     * @param password password
     * @throws Exception
     */
    @Override
    public void createUser(char[] username, char[] email, char[] password) throws Exception {
        HttpPost createUserrequest = new HttpPost(SERVER_ADDRESS + "/user");
        StringEntity informationToSend = new StringEntity("{\"username\": \"" + CharBuffer.wrap(username) + "\",\"email\": \"" + CharBuffer.wrap(email) + "\",\"password\": \"" + CharBuffer.wrap(password) + "\" }");
        sendRequest(informationToSend, createUserrequest);
    }

    /**
     * Gather user information and return the user
     *
     * @return the user
     * @throws Exception
     */
    public User getUserInformation() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(SERVER_ADDRESS + "/user/" + idUser);
        httpget.addHeader("token", this.token);
        CloseableHttpResponse response = httpclient.execute(httpget);

        String result = EntityUtils.toString(response.getEntity());
        JSONObject answerJSON = new JSONObject(result);
        JSONObject JSONtest = (JSONObject) answerJSON.get("user");

        List<Group> groups = new ArrayList<Group>();
        for (Object test : JSONtest.getJSONArray("groups")) {
            groups.add(new Group((String) ((JSONObject) test).get("name"), (String) ((JSONObject) test).get("right"), (Integer) ((JSONObject) test).get("id")));
        }

        User userProfile = new User((Integer) JSONtest.get("id"), (String) JSONtest.get("username"), (String) JSONtest.get("email"), groups);

        return userProfile;
    }

    /**
     * Create a folder that will contains passwords
     *
     * @param folderName folder name
     * @param safeIndex  the safe index
     * @throws Exception
     */
    @Override
    public void createFolder(String folderName, int safeIndex) throws Exception {
        // Server side
        HttpPost createFolderRequest = new HttpPost(SERVER_ADDRESS + "/folder");
        StringEntity informationToSend = new StringEntity("{\"userId\": \"" + idUser + "\",\"name\": \"" + CharBuffer.wrap(folderName).toString() + "\",\"passwords\": [] }");
        createFolderRequest.addHeader("token", this.token);
        JSONObject answerJSON = sendRequest(informationToSend, createFolderRequest);

        // Client side
        safe.get(safeIndex).addFolder(folderName.toCharArray(), (Integer) answerJSON.get("createdId"));
    }

    /**
     * Create a group folder that will contains passwords shared by all group members
     *
     * @param folderName folder name
     * @param groupId    the group id
     * @param safeIndex  the safe index
     * @throws Exception
     */
    public void createGroupFolder(String folderName, int groupId, int safeIndex) throws Exception {

        // Server side
        HttpPost createFolderRequest = new HttpPost(SERVER_ADDRESS + "/folder");
        StringEntity informationToSend = new StringEntity("{\"groupId\": \"" + groupId + "\",\"name\": \"" + CharBuffer.wrap(folderName).toString() + "\",\"passwords\": [] }");
        createFolderRequest.addHeader("token", this.token);
        JSONObject answerJSON = sendRequest(informationToSend, createFolderRequest);

        // Client side
        safe.get(safeIndex).addFolder(folderName.toCharArray(), (Integer) answerJSON.get("createdId"));
    }

    /**
     * Delete a folder
     *
     * @param selectedFolderNumber the selected folder position
     * @param safeIndex            the safe index
     * @throws Exception
     */
    @Override
    public void deleteFolder(int selectedFolderNumber, int safeIndex) throws Exception {
        int idFolder = safe.get(safeIndex).getFolderList().get(selectedFolderNumber).getId();

        // Server side
        HttpDelete deleteEntryRequest = new HttpDelete(SERVER_ADDRESS + "/folder/" + idFolder);
        deleteEntryRequest.addHeader("token", this.token);

        // The entity is not needed since it's a DELETE request
        sendRequest(null, deleteEntryRequest);

        // Client side
        safe.get(safeIndex).deleteFolder(selectedFolderNumber);
    }

    /**
     * Edit a folder name
     *
     * @param folderName the new name of the folder
     * @param index      the index of the folder to change the name of
     * @param safeIndex  the safe index
     * @throws Exception
     */
    @Override
    public void editFolder(char[] folderName, int index, int safeIndex) throws Exception {
        // Server side
        HttpPut editFolderRequest = new HttpPut(SERVER_ADDRESS + "/folder/" + safe.get(safeIndex).getFolderList().get(index).getId());
        StringEntity informationToSend = new StringEntity("{\"name\": \"" + CharBuffer.wrap(folderName).toString() + "\"}");
        editFolderRequest.addHeader("token", this.token);
        sendRequest(informationToSend, editFolderRequest);

        // Client side
        safe.get(safeIndex).editFolder(folderName, index);
    }

    /**
     * Create an entry
     *
     * @param newEntry             the entry created
     * @param selectedFolderNumber the selected folder number
     * @param safeIndex            the safe index
     * @throws Exception
     */
    @Override
    public void addEntry(Entry newEntry, int selectedFolderNumber, int safeIndex) throws Exception {
        // TODO: Put id from server
        int idFolder = safe.get(safeIndex).getFolderList().get(selectedFolderNumber).getId();

        // Server side
        newEntry.encryptEntry(safe.get(safeIndex).getSafePassword());

        HttpPost addEntryrequest = new HttpPost(SERVER_ADDRESS + "/folder/" + idFolder + "/password");
        StringEntity informationToSend = new StringEntity(newEntry.JSONentry());
        addEntryrequest.addHeader("token", this.token);
        JSONObject answerJSON = sendRequest(informationToSend, addEntryrequest);

        newEntry.decryptEntry(safe.get(safeIndex).getSafePassword());

        // Client side
        newEntry.setId((Integer) answerJSON.get("createdId"));
        safe.get(safeIndex).getFolderList().get(selectedFolderNumber).addEntry(newEntry);
    }

    /**
     * Edit an entry
     *
     * @param actualEntry the actual entry
     * @param editedEntry the edited entry
     * @param safeIndex   the safe index
     * @throws Exception
     */
    @Override
    public void editEntry(Entry actualEntry, Entry editedEntry, int safeIndex) throws Exception {
        // Update in the Safe
        actualEntry.setUsername(editedEntry.getUsername());
        actualEntry.setEntryName(editedEntry.getEntryName());
        actualEntry.setClearPassword(editedEntry.getClearPassword());
        actualEntry.setTarget(editedEntry.getTarget());
        actualEntry.setNotes(editedEntry.getNotes());
        actualEntry.setIcon(editedEntry.getIcon());

        // Update on Server
        actualEntry.encryptEntry(safe.get(safeIndex).getSafePassword());

        HttpPut editEntryrequest = new HttpPut(SERVER_ADDRESS + "/password/" + actualEntry.getId());
        StringEntity informationToSend = new StringEntity(actualEntry.JSONentry());
        // TODO: Do I have to put token here ?
        editEntryrequest.addHeader("token", this.token);
        sendRequest(informationToSend, editEntryrequest);

        actualEntry.decryptEntry(safe.get(safeIndex).getSafePassword());
    }

    /**
     * Delete an entry
     *
     * @param selectedFolderNumber the index of the selected entry
     * @param indexOfEntryToRemove the index of the entry to remove
     * @param safeIndex            the safe index
     * @throws Exception
     */
    @Override
    public void deleteEntry(int selectedFolderNumber, int indexOfEntryToRemove, int safeIndex) throws Exception {
        int idPassword = safe.get(safeIndex).getFolderList().get(selectedFolderNumber).getEntrylist().get(indexOfEntryToRemove).getId();

        // Server side
        HttpDelete deleteEntryRequest = new HttpDelete(SERVER_ADDRESS + "/password/" + idPassword);
        deleteEntryRequest.addHeader("token", this.token);
        sendRequest(null, deleteEntryRequest);

        // Client side
        safe.get(safeIndex).getFolderList().get(selectedFolderNumber).removeEntry(indexOfEntryToRemove);
    }

    /**
     * Gather all user data
     *
     * @param password the Safe group password
     * @return a Safe containing all the data
     * Source: https://www.testingexcellence.com/how-to-parse-json-in-java/
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
     * Gather all group data
     *
     * @param password the Safe group password
     * @param groupId  the group id
     * @return a Safe containing all the data
     * Source: https://www.testingexcellence.com/how-to-parse-json-in-java/
     */
    public Safe getGroupData(char[] password, int groupId) throws Exception {
        try {
            String result = "";
            Safe safe = new Safe();
            byte[] iv = new byte[16];

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(SERVER_ADDRESS + "/group/" + groupId + "/folders");
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
     * Create a group
     *
     * @param groupName the name of the group to create
     * @throws Exception
     */
    @Override
    public void createGroup(char[] groupName) throws Exception {
        // Server side
        HttpPost addEntryRequest = new HttpPost(SERVER_ADDRESS + "/group");
        StringEntity informationToSend = new StringEntity("{ \"name\": \"" + CharBuffer.wrap(groupName).toString() + "\" }");
        addEntryRequest.addHeader("token", this.token);
        sendRequest(informationToSend, addEntryRequest);

        // Client side
        this.user.addGroup(new Group(CharBuffer.wrap(groupName).toString(), "ADMIN", 0));
    }

    /**
     * Delete a group
     *
     * @param selectedGroupNumber the position of the group to delete
     * @throws Exception
     */
    public void deleteGroup(int selectedGroupNumber) throws Exception {
        int idGroup = user.getGroups().get(selectedGroupNumber).getIdGroup();

        // Server side
        HttpDelete deleteEntryRequest = new HttpDelete(SERVER_ADDRESS + "/group/" + idGroup);
        deleteEntryRequest.addHeader("token", this.token);
        sendRequest(null, deleteEntryRequest);

        // Client side
        user.deleteGroup(selectedGroupNumber);
    }

    /**
     * Renew the token
     *
     * @throws Exception
     */
    public void renewToken() throws Exception {
        HttpPost tokenrequest = new HttpPost(SERVER_ADDRESS + "/renew");
        tokenrequest.addHeader("token", this.token);
        JSONObject tokenStatus = sendRequest(null, tokenrequest);

        if (!tokenStatus.get("errorCode").equals(0)) {
            throw new Exception(tokenStatus.get("message").toString());
        } else {
            this.token = (String) tokenStatus.get("token");
        }
    }

    /**
     * Send an abstract HTTP request, method can be: POST, DELETE, GET, etc...
     * If the method support entity (not DELETE for example), the provided entity will be added
     * Source: https://stackoverflow.com/questions/5769717/how-can-i-get-an-http-response-body-as-a-string-in-java +
     * https://stackoverflow.com/questions/7181534/http-post-using-json-in-java
     *
     * @param informationToSend information to send to server
     * @param request           request http to server
     * @return return the json from server
     * @throws Exception
     */
    private JSONObject sendRequest(StringEntity informationToSend, HttpRequestBase request) throws Exception {
        request.addHeader("content-type", "application/json");

        if (request instanceof HttpEntityEnclosingRequestBase) {
            ((HttpEntityEnclosingRequestBase) request).setEntity(informationToSend);
        }

        // Send the request and get the HTTP response
        HttpResponse loginAnswer = httpClient.execute(request);
        HttpEntity test1 = loginAnswer.getEntity();

        String answerJSONString = EntityUtils.toString(test1, "UTF-8");
        JSONObject answerJSON = new JSONObject(answerJSONString);

        detectError(answerJSON);

        return answerJSON;
    }

    /**
     * Read the status code of a response and throw an exception if there is an error
     *
     * @param answerJSON the response to check error for
     * @throws Exception if there is an error
     */
    private void detectError(JSONObject answerJSON) throws Exception {
        if (!answerJSON.get("errorCode").equals(0)) {
            throw new Exception(answerJSON.get("message").toString());
        }
    }

}