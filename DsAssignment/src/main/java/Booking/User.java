package Booking;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String username;
    private String password;
    private String role;
    private double[] locationCoordinate;
    
    public User(){}

    public User(String email, String username, String password, String role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.locationCoordinate = new double[] {0, 0};
    }

    public String getUsername() {
        return username;
    }

    public void setLocationCoordinate(double[] locationCoordinate) {
        this.locationCoordinate = locationCoordinate;
    }
    
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public double[] getLocationCoordinate() {
        return locationCoordinate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    
}
