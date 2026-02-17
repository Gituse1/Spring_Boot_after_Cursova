package com.example.service.admin;

import com.example.model.Bus;
import com.example.repository.BusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminBusService {

    private final BusRepository busRepository;


    @Transactional
    public void deleteBus( long id){
        if(!busRepository.existsById(id) ){
            throw  new IllegalArgumentException("Автобус не знайдено");
        }
        busRepository.deleteById(id);
    }
    @Transactional
    public Bus updateBus (long id, Bus newBus){

        if((newBus.getPlateNumber() == null) || newBus.getTotalSeats() == 0 ) {
            throw new IllegalArgumentException("Автобус не знайдено");
        }

        Bus exitingBus=busRepository.findById(id).orElseThrow(()-> new RuntimeException("Автобус з id " + id + " не знайдено"));

        exitingBus.setPlateNumber(newBus.getPlateNumber());
        exitingBus.setTotalSeats(newBus.getTotalSeats());


        return busRepository.save(exitingBus);

    }
    @Transactional
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
