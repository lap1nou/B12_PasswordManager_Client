package ch.heigvd.pro.client;

import java.util.List;

public interface IPasswordCheckerDriver {

    boolean isLeaked(String password);
    List<String> parseLeakedResponse(String response);
}
