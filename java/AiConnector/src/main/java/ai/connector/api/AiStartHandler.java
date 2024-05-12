package ai.connector.api;

import ai.connector.Message;
import ai.connector.producers.ToJarvis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AiStartHandler {
    private final ToJarvis prodJarvis;

    @PostMapping("/start-mission")
    public ResponseEntity<String> NewMission(
            final @RequestBody StartDTO request
    ) {
        log.info("Create connection request received: {}", request.getStart());
        try {
            prodJarvis.sendMessage(
                    new Message("Запрос на начало подготовки к миссии"),
                    "default",
                    "ai-connector",
                    "jarvis"
            );
        } catch (Exception ex) {
            log.error("Error: {}", ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/enemy")
    public ResponseEntity<String> NewEnemy(
            final @RequestBody StartDTO request
    ) {
        log.info("Create connection request received: {}", request.getStart());
        try {
            prodJarvis.sendMessage(
                    new Message("Запрос на активацию оружия"),
                    "default",
                    "ai-connector",
                    "jarvis"
            );
        } catch (Exception ex) {
            log.error("Error: {}", ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/negative")
    public ResponseEntity<String> NewCase(
            final @RequestBody StartDTO request
    ) {
        log.info("Create connection request received: {}", request.getStart());
        try {
            prodJarvis.sendMessage(
                    new Message("Негативный кейс"),
                    "default",
                    "ai-connector",
                    "geo"
            );
        } catch (Exception ex) {
            log.error("Error: {}", ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
        return ResponseEntity.ok("Success");
    }
}