package co.example.LoginDemo.auth.entities;

import javax.persistence.Entity;

public class SignupActivationDTO {
    private String activationCode;

    public SignupActivationDTO(String activationCode){
        this.activationCode = activationCode;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
}
