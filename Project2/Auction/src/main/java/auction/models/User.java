package auction.models;

import java.util.Objects;

public class User {

    private int userId;
    private String userName;
    private String password;
    private String creditCardNumber;
    private int role;


    public User() {
        this.userId = -1;
        this.userName = null;
        this.password = null;
        this.creditCardNumber = null;
        this.role = 1;
    }

    public User(String userName, String password) {
        this.userId = -1;
        this.userName = userName;
        this.password = password;
        this.creditCardNumber = null;
        this.role = 1;
    }

    public User(int id, String userName, String password, String creditCardNumber, int role) {
        this.userId = id;
        this.userName = userName;
        this.password = password;
        this.creditCardNumber = creditCardNumber;
        this.role = role;
    }

    public User(String userName, String password, String ccNumber, int role) {
        this.userId = -1;
        this.userName = userName;
        this.password = password;
        this.creditCardNumber = ccNumber;
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId &&
                userName.equals(user.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName);
    }

    @Override
    public String toString() {
        String roleString;
        if (role == 1)
            roleString = "User";
        else if (role == 2)
            roleString = "Admin";
        else if (role == 3)
            roleString = "Banned";
        else
            roleString = "Invalid Role";
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", role=" + roleString +
                '}';
    }
}