public class Users {
    private int id;
    private String name;
    private int role_id;

    @Override
    public String toString() {
        return "users{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role_id=" + role_id +
                '}';
    }

    public Users() {
    };

    public Users(int id, String name, int role_id) {
        this.id = id;
        this.name = name;
        this.role_id = role_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }
}
