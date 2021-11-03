package academy.mischok.ipcalculatorweb.web;

import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import academy.mischok.ipcalculatorweb.domain.IpAddress;
import academy.mischok.ipcalculatorweb.domain.Subnetmask;

@Controller
public class NetworkController {

    @GetMapping("/network")
    public String getOverview(@RequestParam String ip, @RequestParam String snm, Model model) {

        try {
            IpAddress ipParsed = new IpAddress(ip);
            Subnetmask snmParsed = new Subnetmask(snm);
            IpAddress netId = ipParsed.logicalAnd(snmParsed);
            IpAddress broadcastIp = calculateBroadcastIp(netId, snmParsed);

            model.addAttribute("ip", ipParsed);
            model.addAttribute("snm", snmParsed);
            model.addAttribute("netId", netId);
            model.addAttribute("broadcastIp", broadcastIp);

            return "network-overview";
        } catch (IllegalArgumentException e) {
            return "network-error";
        }
    }

    @GetMapping("/input")
    public String getInput() {
        return "network-input";
    }

    private IpAddress calculateBroadcastIp(IpAddress netId, Subnetmask subnetmask) {
        Objects.requireNonNull(netId);
        Objects.requireNonNull(subnetmask);

        IpAddress invertedSnm = subnetmask.invert();

        int first = netId.getFirst() + invertedSnm.getFirst();
        int second = netId.getSecond() + invertedSnm.getSecond();
        int third = netId.getThird() + invertedSnm.getThird();
        int fourth = netId.getFourth() + invertedSnm.getFourth();

        return new IpAddress(first, second, third, fourth);
    }
}
