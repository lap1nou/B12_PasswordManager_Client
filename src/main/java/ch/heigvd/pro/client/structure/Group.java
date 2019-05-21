package ch.heigvd.pro.client.structure;

public class Group {
    private int idGroup;
    private String name;
    private String right;

    public Group(String name, String right, int idGroup) {
        this.name = name;
        this.right = right;
        this.idGroup = idGroup;
    }

    public String getName() {
        return name;
    }

    public String getRight() {
        return right;
    }

    public int getIdGroup() {
        return idGroup;
    }
}
