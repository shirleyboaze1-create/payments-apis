package com.example.payments_api.controller;

import com.example.payments_api.model.Payment;
import com.example.payments_api.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Criar pagamento
    @PostMapping("/payments")
    public ResponseEntity<?> criarPagamento(@RequestBody Payment payment) {

        if (payment.getAmount() == null || payment.getAmount().doubleValue() <= 0) {
            return ResponseEntity.badRequest().body("Valor inválido.");
        }

        if (payment.getPhoneNumber() == null || payment.getPhoneNumber().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Número de telefone obrigatório.");
        }

        if (payment.getReference() == null || payment.getReference().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Referência obrigatória.");
        }

        try {

            Payment novoPagamento = paymentService.iniciarPagamento(
                    payment.getAmount(),
                    payment.getPhoneNumber(),
                    payment.getReference()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(novoPagamento);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar pagamento.");
        }
    }

    // Consultar pagamento pelo ID
    @GetMapping("/payments/{id}")
    public ResponseEntity<?> consultarPagamento(@PathVariable String id) {

        try {

            UUID uuid = UUID.fromString(id);

            Optional<Payment> payment = paymentService.buscarPorId(uuid);

            if (payment.isPresent()) {
                return ResponseEntity.ok(payment.get());
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Pagamento não encontrado.");

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body("ID inválido.");

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao consultar pagamento.");
        }
    }

    // Webhook do M-Pesa
    @PostMapping("/webhooks/mpesa")
    public ResponseEntity<String> webhookMpesa(@RequestBody Map<String, Object> callbackData) {

        try {

            if (!callbackData.containsKey("reference")
                    || !callbackData.containsKey("status")) {

                return ResponseEntity.badRequest()
                        .body("Dados inválidos.");
            }

            String reference = callbackData.get("reference").toString();

            String transactionId = callbackData.containsKey("transactionId")
                    ? callbackData.get("transactionId").toString()
                    : "N/A";

            String status = callbackData.get("status").toString();

            Optional<Payment> resultado =
                    paymentService.processarCallbackMpesa(
                            reference,
                            transactionId,
                            status
                    );

            if (resultado.isPresent()) {
                return ResponseEntity.ok("Webhook processado.");
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Referência não encontrada.");

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro no webhook.");
        }
    }
}