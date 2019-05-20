package ch.heigvd.pro.client.structure;

import java.util.ArrayList;
import java.util.List;

public class User {
    int id;
    String username;
    String email;
    List<Group> groups;

    public User(int id, String username, String email, List<Group> groups){
        this.id = id;
        this.username = username;
        this.email = email;
        this.groups = groups;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void addGroup(Group group){
        this.groups.add(group);
    }
}
