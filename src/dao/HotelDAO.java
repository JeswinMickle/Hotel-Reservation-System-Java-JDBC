package dao;

import dto.*;

import java.time.LocalDate;
import java.util.List;

public interface HotelDAO {

    List<RoomDTO> getAvailableRooms(LocalDate checkIn, LocalDate checkOut);

    int addCustomer(CustomerDTO customer);

    double getRoomPrice(int roomId);

    boolean bookRoom(BookingDTO booking);

    boolean cancelBooking(int bookingId);

    BookingDTO getBill(int bookingId);
}
