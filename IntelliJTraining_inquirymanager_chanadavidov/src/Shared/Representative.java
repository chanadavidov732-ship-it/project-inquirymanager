package Shared;

public class Representative {
    static int RepresentativeCode = 0;

    private int code;
    private String name;
    private int id;


    public Representative() {
        code = Representative.RepresentativeCode++;
    }

    public Representative(String name, int id) {
        code = Representative.RepresentativeCode++;
        this.name = name;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
