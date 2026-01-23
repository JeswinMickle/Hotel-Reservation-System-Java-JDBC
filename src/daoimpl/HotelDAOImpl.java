package daoimpl;

import dao.HotelDAO;
import dto.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HotelDAOImpl implements HotelDAO {

    private Connection getConnection() throws Exception {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/hotel_db",
                "root",
                "root"
        );
    }

    // 1️⃣ Room availability
    @Override
    public List<RoomDTO> getAvailableRooms(LocalDate in, LocalDate out) {

        List<RoomDTO> list = new ArrayList<>();

        String sql = """
            SELECT * FROM rooms
            WHERE room_id NOT IN (
                SELECT room_id FROM bookings
                WHERE NOT (? >= check_out OR ? <= check_in)
            )
        """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(in));
            ps.setDate(2, Date.valueOf(out));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RoomDTO r = new RoomDTO();
                r.setRoomId(rs.getInt("room_id"));
                r.setRoomType(rs.getString("room_type"));
                r.setPricePerDay(rs.getDouble("price_per_day"));
                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 2️⃣ Add customer
    @Override
    public int addCustomer(CustomerDTO c) {

        String sql = "INSERT INTO customer(name,phone) VALUES (?,?)";

        try (Connection con = getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getName());
            ps.setString(2, c.getPhone());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 3️⃣ Room price
    @Override
    public double getRoomPrice(int roomId) {

        try (Connection con = getConnection();
             PreparedStatement ps =
                     con.prepareStatement("SELECT price_per_day FROM rooms WHERE room_id=?")) {

            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getDouble(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 4️⃣ Book room
    @Override
    public boolean bookRoom(BookingDTO b) {

        String sql =
                "INSERT INTO bookings(customer_id,room_id,check_in,check_out,total_amount) " +
                "VALUES (?,?,?,?,?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, b.getCustomerId());
            ps.setInt(2, b.getRoomId());
            ps.setDate(3, b.getCheckIn());
            ps.setDate(4, b.getCheckOut());
            ps.setDouble(5, b.getTotalAmount());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5️⃣ Cancel
    @Override
    public boolean cancelBooking(int id) {

        try (Connection con = getConnection();
             PreparedStatement ps =
                     con.prepareStatement("DELETE FROM bookings WHERE booking_id=?")) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 6️⃣ Bill
    @Override
    public BookingDTO getBill(int id) {

        String sql = "SELECT * FROM bookings WHERE booking_id=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                BookingDTO b = new BookingDTO();
                b.setBookingId(id);
                b.setCustomerId(rs.getInt("customer_id"));
                b.setRoomId(rs.getInt("room_id"));
                b.setCheckIn(rs.getDate("check_in"));
                b.setCheckOut(rs.getDate("check_out"));
                b.setTotalAmount(rs.getDouble("total_amount"));
                return b;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
