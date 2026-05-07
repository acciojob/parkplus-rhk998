package com.driver.services.impl;

import com.driver.Exceptions.NoSpotAvailable;
import com.driver.Exceptions.ParkingLotNotAvailable;
import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    private final ParkingLotRepository parkingLotRepository;
    private final SpotRepository spotRepository1;


    @Autowired
    public ParkingLotServiceImpl(ParkingLotRepository parkingLotRepository, SpotRepository spotRepository1) {
        this.parkingLotRepository = parkingLotRepository;
        this.spotRepository1 = spotRepository1;
    }

    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setAddress(address);
        parkingLot.setName(name);
        return parkingLotRepository.save(parkingLot);
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        Spot spot = new Spot();
        ParkingLot parkingLot = parkingLotRepository.findById(parkingLotId).orElseThrow(()->new ParkingLotNotAvailable("parking lot not available"));
        List<Spot> spotList = new ArrayList<>();
        spotList = parkingLot.getSpotList();
        spot.setParkingLot(parkingLot);
        spot.setPricePerHour(pricePerHour);
        SpotType spotType = SpotType.OTHERS ;
        if(numberOfWheels == 2){
            spotType = SpotType.TWO_WHEELER;
        }else if(numberOfWheels == 4){
            spotType = SpotType.FOUR_WHEELER;
        }else{
            throw new NoSpotAvailable("No spot is available for a " + numberOfWheels +" wheeler");
        }
        spot.setSpotType(spotType);
        spotList.add(spot);
        parkingLotRepository.save(parkingLot);
        return spotRepository1.save(spot);
    }

    @Override
    public void deleteSpot(int spotId) {

        Spot spot = spotRepository1.findById(spotId)
                .orElseThrow(() ->
                        new NoSpotAvailable("No spot is available with Id " + spotId));

        ParkingLot parkingLot = spot.getParkingLot();

        List<Spot> spotList = parkingLot.getSpotList();

        spotList.removeIf(s -> s.getId() == spotId);

        parkingLotRepository.save(parkingLot);

        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository.findById(parkingLotId)
                .orElseThrow(() -> new ParkingLotNotAvailable("ParkingLot Not Available for Id " + parkingLotId));
        return parkingLot.getSpotList()
                .stream()
                .filter(spot -> spot.getId() == spotId)
                .findFirst()
                .map(spot -> {
                    spot.setPricePerHour(pricePerHour);
                    return spotRepository1.save(spot);
                })
                .orElseThrow(() -> new NoSpotAvailable( "No spot is available with Id" + spotId));
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository.deleteById(parkingLotId);
    }
}
