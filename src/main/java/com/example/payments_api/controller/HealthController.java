@com.example.payments_api.controller; 

@org.springframework.web.bind.annotation.RestController
public class HealthController {
    @org.springframework.web.bind.annotation.GetMapping("/")
    public java.util.Map<String, String> healthCheck() {
        return java.util.Collections.singletonMap("status", "API de Pagamentos Ativa");
    }
}