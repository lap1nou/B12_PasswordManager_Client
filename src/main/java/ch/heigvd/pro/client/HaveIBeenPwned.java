package ch.heigvd.pro.client;

import ch.heigvd.pro.client.crypto.Crypto;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HaveIBeenPwned implements IPasswordCheckerDriver {
    // Used this doc : http://hc.apache.org/httpcomponents-client-ga/quickstart.html
    private final String apiURL = "https://api.pwnedpasswords.com/range/";
    private final int hashLength = 35;
    private CloseableHttpClient httpClient = HttpClients.createDefault();

    /**
     * This function query the HaveIBeenPwned database in order to know if the given password was leaked somewhere.
     * To do that securely, the API use the k-anonimity system (https://en.wikipedia.org/wiki/K-anonymity), we basically
     * query the API with the first five characters of the hashed password and the API give us back all the hash that
     * matched, we then just need to compare the rest of the hashed password with all the results.
     *
     * @param password the checked password
     * @return true if the password was leaked somewhere in the database, false otherwise
     */
    public boolean isLeaked(String password) {
        HttpGet test = new HttpGet(apiURL + Crypto.fiveAnonimitySHA1(password));

        try {
            HttpResponse answer = httpClient.execute(test);
            HttpEntity entity = answer.getEntity();

            List<String> result = parseLeakedResponse(EntityUtils.toString(entity));
            String toMatch = Crypto.restOfFiveAnonimitySHA1(password);

            for (String hash : result) {
                if (hash.substring(0, hashLength).equals(toMatch)) {
                    return true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * This function simply take all the big string that the API answered with and put each line in a List of String.
     *
     * @param responseToParse the big String to parse
     * @return A String list, where each string is a line of the answer
     */
    public List<String> parseLeakedResponse(String responseToParse) {
        String[] tmp = responseToParse.split("\n");
        return Arrays.asList(tmp);
    }

}
