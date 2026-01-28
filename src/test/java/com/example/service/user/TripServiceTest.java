package com.example.service.user;

import com.example.model.Trip;
import com.example.repository.TripRepository;
import com.example.service.AuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private  TripRepository tripRepository;


    @Mock
    private AuditService auditService;

    @InjectMocks
    private TripService tripService;


    @BeforeEach
    public void before(){
        lenient().doNothing().when(auditService).log(any(), any());
        lenient().doNothing().when(auditService).log(any(),any(),anyString());
    }


    @Test
    public void searchTripsTest(){

        Trip mockTrip = new Trip();
        List<Trip> mockResult = List.of(mockTrip);

        when(tripRepository.findTripsByRouteAndDate(eq(1L), eq(2L), any(LocalDate.class)))
                .thenReturn(mockResult);

        List<Trip> actual = tripService.searchTrips(1L,2L,"2025-01-20");
        assertEquals(mockResult, actual);

        verify(tripRepository).findTripsByRouteAndDate(eq(1L), eq(2L), any(LocalDate.class));

    }


    @ParameterizedTest
    @ValueSource(strings = {"invalid-date", "20-12-2025", "", "   ", "2025.01.20"})
    void searchTrips_ThrowException(String invalidDateStr) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            tripService.searchTrips(1L, 2L, invalidDateStr);
        });
    }


    @ParameterizedTest
    @CsvSource({
            "1, 2, '2025-01-20'", // Звичайний рейс
            "5, 10, '2025-02-15'", // Інший напрямок
            "0, 0, '2025-12-31'"   // Граничні значення
    })
    void searchTrips_ShouldCallRepositoryWithCorrectIds(Long fromId, Long toId, String date) {
        tripService.searchTrips(fromId, toId, date);

        verify(tripRepository).findTripsByRouteAndDate(eq(fromId), eq(toId), any(LocalDate.class));
    }


}