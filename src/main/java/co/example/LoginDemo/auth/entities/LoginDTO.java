package co.example.LoginDemo.auth.entities;

public class LoginDTO {
     /** This is the user email and password */
    private String email;
    private String password;

    public LoginDTO ( String email, String password){
        this.email = email ;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
