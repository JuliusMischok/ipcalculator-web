package academy.mischok.ipcalculatorweb.web.forms;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

public class NetworkInputForm {

    @NotBlank
    @Length(min = 7)
    private String ip;

    @NotBlank
    private String snm;

    public String getIp() {
        return ip;
    }

    @SuppressWarnings("unused") // used from thymeleaf
    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSnm() {
        return snm;
    }

    @SuppressWarnings("unused") // used from thymeleaf
    public void setSnm(String snm) {
        this.snm = snm;
    }

    @Override
    public String toString() {
        return "NetworkInputForm{" +
                "ip='" + ip + '\'' +
                ", snm='" + snm + '\'' +
                '}';
    }
}
