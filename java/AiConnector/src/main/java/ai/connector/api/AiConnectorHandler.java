package ai.connector.api;

import ai.connector.Connection;
import ai.connector.producers.ToRedirector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AiConnectorHandler {
    private final ToRedirector prodConnection;

    @PostMapping("/create-connection")
    public ResponseEntity<String> NewDevice(
            final @RequestBody ConnectionDTO request
    ) {
        log.info("Create connection request received: {}", request.getConnectionCommand());
        try {
            prodConnection.send(
                    new Connection(request.getConnectionCommand()),
                    "create-connection",
                    "ai-connector",
                    "redirector"
            );
        } catch (Exception ex) {
            log.error("Error: {}", ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
        return ResponseEntity.ok("Success");
    }
}