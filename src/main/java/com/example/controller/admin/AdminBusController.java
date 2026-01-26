package com.example.controller.admin;


import com.example.model.Bus;
import com.example.repository.BusRepository;
import com.example.service.admin.AdminBusService;
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
   private final AdminBusService adminBusService;

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

       adminBusService.deleteBus(id);
       return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<Bus> updateBus (@PathVariable long id, @RequestBody Bus newBus){

       Bus updateBus = adminBusService.updateBus(id,newBus);
       return ResponseEntity.ok(updateBus);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<Bus> updateBusCapacity(@PathVariable long id, @RequestBody Map<String, Integer> updates){

       Bus updateBus = adminBusService.updateBusCapacity(id,updates);
       return ResponseEntity.ok(updateBus);

    }

}
