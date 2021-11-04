package academy.mischok.ipcalculatorweb.web.forms;

import javax.validation.constraints.NotBlank;

public class NetworkInputForm {

    @NotBlank
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
