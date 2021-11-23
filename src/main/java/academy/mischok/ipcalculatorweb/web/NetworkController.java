package academy.mischok.ipcalculatorweb.web;

import java.util.Objects;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import academy.mischok.ipcalculatorweb.domain.IpAddress;
import academy.mischok.ipcalculatorweb.domain.Subnetmask;
import academy.mischok.ipcalculatorweb.web.forms.NetworkInputForm;

@Controller
public class NetworkController {

    @GetMapping(value = "/")
    public String index() {
        return "redirect:/input";
    }

    @PostMapping(value = "/network")
    public String getOverview(@Valid NetworkInputForm networkInputForm, BindingResult bindingResult, Model model) {

        System.out.println("form: " + networkInputForm);

        validate(networkInputForm, bindingResult);

        System.out.println("bindingResult: " + bindingResult);

        if (bindingResult.hasErrors()) {
            return "network-input";
        }

        try {
            IpAddress ipParsed = new IpAddress(networkInputForm.getIp());
            Subnetmask snmParsed = new Subnetmask(networkInputForm.getSnm());
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

    private void validate(NetworkInputForm networkInputForm, BindingResult bindingResult) {
        Objects.requireNonNull(networkInputForm);
        Objects.requireNonNull(bindingResult);

        if (!bindingResult.hasFieldErrors("ip")) {
            try {
                // check, if IP input is valid
                new IpAddress(networkInputForm.getIp());
            } catch (IllegalArgumentException e) {
                bindingResult.rejectValue("ip", "invalid.ip", "IP Format ungültig");
            }
        }

        if (!bindingResult.hasFieldErrors("snm")) {
            try {
                new Subnetmask(networkInputForm.getSnm());
            } catch (IllegalArgumentException e) {
                bindingResult.rejectValue("snm", "invalid.snm", "Ungültige Subnetzmaske");
            }
        }
    }

    @GetMapping("/input")
    public String getInput(Model model) {
        NetworkInputForm networkInputForm = new NetworkInputForm();

        /*
        networkInputForm.setIp("192.168.2.1");
        */

        model.addAttribute("networkInputForm", networkInputForm);

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

