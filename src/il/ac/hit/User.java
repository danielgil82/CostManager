package il.ac.hit;

public class User
{
    private int id;
    private String fullName;
    private String usersPassword;

    public User(int id, String fullName, String usersPassword)
    {
      id = id;
      fullName = fullName;
      usersPassword = usersPassword;
    }

    public User(String fullName, String usersPassword)
    {
        fullName = fullName;
        usersPassword = usersPassword;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public String getUsersPassword()
    {
        return usersPassword;
    }

    public void setUsersPassword(String usersPassword)
    {
        this.usersPassword = usersPassword;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", usersPassword='" + usersPassword + '\'' +
                '}';
    }
}
