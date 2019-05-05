package ch.heigvd.pro.client.password;

import java.util.List;

public interface IPasswordCheckerDriver {

    boolean isLeaked(char[] password);
    List<String> parseLeakedResponse(String response);
}
