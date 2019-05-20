package ch.heigvd.pro.client.structure;

public class Group {
    private String name;
    private String right;

    public Group( String name, String right){
        this.name = name;
        this.right = right;
    }

    public String getName() {
        return name;
    }

    public String getRight() {
        return right;
    }
}
