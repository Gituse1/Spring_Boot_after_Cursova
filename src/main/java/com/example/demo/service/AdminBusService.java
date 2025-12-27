package com.example.demo.service;

import com.example.demo.model.Bus;
import com.example.demo.repository.BusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminBusService {

    private final BusRepository busRepository;


    public void deleteBus( long id){
        if(!busRepository.existsById(id) ){
            throw  new IllegalArgumentException("Автобус не знайдено");
        }
        busRepository.deleteById(id);
    }

    public Bus updateBus (long id, Bus newBus){

        if((newBus.getPlateNumber() == null) || newBus.getTotalSeats() == 0 ) {
            throw new IllegalArgumentException("Автобус не знайдено");
        }

        Bus exitingBus=busRepository.findById(id).orElseThrow(()-> new RuntimeException("Автобус з id " + id + " не знайдено"));

        exitingBus.setPlateNumber(newBus.getPlateNumber());
        exitingBus.setTotalSeats(newBus.getTotalSeats());


        return busRepository.save(exitingBus);

    }

    public Bus updateBusCapacity( long id,  Map<String, Integer> updates){
        Integer newCapacity = updates.get("capacity");

        if(newCapacity<= 0){
            throw new IllegalArgumentException("Кількість місць введено не правильно");
        }
        Bus bus= busRepository.findById(id).orElseThrow(() -> new RuntimeException("Автобус не знайдено"));
        bus.setTotalSeats(newCapacity);
        return  busRepository.save(bus);

    }
}
