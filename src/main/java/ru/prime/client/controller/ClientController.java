package ru.prime.client.controller;

import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.prime.client.model.ConfigurationParams;
import ru.prime.client.stomp.StompConfiguration;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ClientController {

    private final StompConfiguration stompConfiguration;

    private StompSession stompSession;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("configurationParams", new ConfigurationParams());
        return "index";
    }

    @PostMapping("/connect")
    @ResponseBody
    public String connect(@ModelAttribute("configurationParams") ConfigurationParams configurationParams) throws Exception {
        if (stompSession == null || !stompSession.isConnected()) {
            stompSession = stompConfiguration.connect(configurationParams);
            return stompSession.getSessionId();
        }
        return null;
    }

    @GetMapping("/disconnect")
    @ResponseBody
    public void disconnect() {
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.disconnect();
            log.info("Session {} closed", stompSession.getSessionId());
            stompSession = null;
        }
    }

    @GetMapping("/session")
    @ResponseBody
    public String getSession() {
        if (stompSession != null && stompSession.isConnected()) {
            return stompSession.getSessionId();
        }
        return null;
    }

    @GetMapping("/clearLogs")
    @ResponseBody
    public void clearLogs() throws Exception {
        String logfile = System.getProperty("user.dir") + "/logs/spring.log";
        @Cleanup BufferedWriter writer =  Files.newBufferedWriter(Paths.get(logfile));
        writer.write("");
        writer.flush();
    }
}
