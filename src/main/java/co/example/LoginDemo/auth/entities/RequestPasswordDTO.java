package co.example.LoginDemo.auth.entities;

public class RequestPasswordDTO {
    private String email;

    public void  RestorePasswordDTO (String email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
