package com.driver.services.impl;

import com.driver.Exceptions.InsufficientAmount;
import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;
    @Autowired
    SpotRepository spotRepository;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        if (mode == null || !mode.matches("(?i)^(cash|card|upi)$")) {
            throw new RuntimeException("Payment mode not detected");
        }
        Payment payment = new Payment();
        Reservation reservation = reservationRepository2.findById(reservationId).orElse(null);
        payment.setReservation(reservation);
        payment.setPaymentMode(PaymentMode.valueOf(mode));
        Spot spot = spotRepository.findByReservationId(reservationId);
        int price = spot.getPricePerHour();
        if(amountSent < price) throw new InsufficientAmount("Insufficient Amount");
        payment.setPaymentCompleted(true);
        return paymentRepository2.save(payment);
    }
}
