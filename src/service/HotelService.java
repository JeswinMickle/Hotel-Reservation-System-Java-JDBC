package service;

import dao.*;
import daoimpl.*;
import dto.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class HotelService {

    HotelDAO dao = new HotelDAOImpl();

    public List<RoomDTO> checkAvailability(LocalDate in, LocalDate out) {
        return dao.getAvailableRooms(in, out);
    }

    public boolean bookRoom(String name, String phone,
                            int roomId, LocalDate in, LocalDate out) {

        CustomerDTO c = new CustomerDTO();
        c.setName(name);
        c.setPhone(phone);

        int customerId = dao.addCustomer(c);

        long days = ChronoUnit.DAYS.between(in, out);
        double price = dao.getRoomPrice(roomId);

        BookingDTO b = new BookingDTO();
        b.setCustomerId(customerId);
        b.setRoomId(roomId);
        b.setCheckIn(Date.valueOf(in));
        b.setCheckOut(Date.valueOf(out));
        b.setTotalAmount(days * price);

        return dao.bookRoom(b);
    }

    public boolean cancelBooking(int id) {
        return dao.cancelBooking(id);
    }

    public BookingDTO viewBill(int id) {
        return dao.getBill(id);
    }
}
