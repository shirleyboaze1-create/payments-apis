package com.example.payments_api.service;

import com.example.payments_api.model.Payment;
import com.example.payments_api.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MpesaService mpesaService;

    public PaymentService(PaymentRepository paymentRepository, MpesaService mpesaService) {
        this.paymentRepository = paymentRepository;
        this.mpesaService = mpesaService;
    }

    public Payment iniciarPagamento(BigDecimal amount, String phoneNumber, String reference) {
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setPhoneNumber(phoneNumber);
        payment.setReference(reference);
        payment.setStatus("PENDING");
        
        Payment pagamentoSalvo = paymentRepository.save(payment);
        Map<String, Object> respostaMpesa = mpesaService.enviarPedidoPagamento(amount, phoneNumber, reference);
        
        return pagamentoSalvo;
    }

    public Optional<Payment> processarCallbackMpesa(String reference, String transactionId, String status) {
        Optional<Payment> paymentOptional = paymentRepository.findByReference(reference);
        
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            payment.setTransactionId(transactionId);
            payment.setStatus(status);
            payment.setUpdatedAt(LocalDateTime.now());
            return Optional.of(paymentRepository.save(payment));
        }
        
        return Optional.empty();
    }

    public Optional<Payment> buscarPorId(UUID id) {
        return paymentRepository.findById(id);
    }
}