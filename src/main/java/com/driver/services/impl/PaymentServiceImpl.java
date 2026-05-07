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


        Reservation reservation = reservationRepository2.findById(reservationId).orElse(null);
        PaymentMode paymentMode = PaymentMode.valueOf(mode);
        Spot spot = reservation.getSpot();
        int bill = spot.getPricePerHour() * reservation.getNumberOfHours();
        if(amountSent < bill){
            throw new InsufficientAmount("Insufficient Amount");
        }
        Payment payment = new Payment();

        payment.setReservation(reservation);
        payment.setPaymentMode(paymentMode);
        payment.setPaymentCompleted(true);
        payment.setPaymentCompleted(true);
        spot.setOccupied(false);
        return paymentRepository2.save(payment);
    }
}
