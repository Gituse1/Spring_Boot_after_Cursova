package com.example.demo.controller;


import com.example.demo.model.Bus;
import com.example.demo.repository.BusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/admin/bus")
@RequiredArgsConstructor
public class AdminBusController {

   private final BusRepository busRepository ;

   @GetMapping
    public ResponseEntity<List<Bus>> getAllBus(){
       return ResponseEntity.ok( busRepository.findAll());
    }

    @PutMapping
    public ResponseEntity<Bus> createBus(@RequestBody Bus bus){
       Bus createdBus = busRepository.save(bus);
       return ResponseEntity.status(HttpStatus.CREATED).body(createdBus);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteBus(@RequestBody long id){
       if( busRepository.existsById(id) ){
           busRepository.deleteById(id);
       }
       else{
           throw  new RuntimeException("Такого автобуса не знайдено");
       }
       return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bus> updateBus (@PathVariable long id, @RequestBody Bus newBus){

       if((newBus.getPlateNumber() == null) || newBus.getTotalSeats() == 0 ) {
           return ResponseEntity.badRequest().build();
       }

        Bus exitingBus=busRepository.findById(id).orElseThrow(()-> new RuntimeException("Автобус з id " + id + " не знайдено"));

        exitingBus.setPlateNumber(newBus.getPlateNumber());
        exitingBus.setTotalSeats(newBus.getTotalSeats());

        Bus updatedBus = busRepository.save(exitingBus);

        return ResponseEntity.ok(updatedBus);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Bus> updateBusCapacity(@PathVariable long id, @RequestBody Map<String, Integer> updates){
        Integer newCapacity = updates.get("capacity");

       if(newCapacity== 0){
           return ResponseEntity.badRequest().build();
       }
       Bus bus= busRepository.findById(id).orElseThrow(() -> new RuntimeException("Автобус не знайдено"));

       bus.setTotalSeats(newCapacity);
       Bus updateBus= busRepository.save(bus);
       return ResponseEntity.ok(updateBus);

    }

}
