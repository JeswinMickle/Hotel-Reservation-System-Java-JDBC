package main;

import dto.RoomDTO;
import service.HotelService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class HotelMain {

    public static void main(String[] args) {

        HotelService service = new HotelService();
        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n1. Check Availability");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View Bill");
            System.out.println("5. Exit");

            int ch = sc.nextInt();

            switch (ch) {

                case 1 -> {
                    System.out.print("Check-in: ");
                    LocalDate in = LocalDate.parse(sc.next());
                    System.out.print("Check-out: ");
                    LocalDate out = LocalDate.parse(sc.next());

                    List<RoomDTO> rooms =
                            service.checkAvailability(in, out);

                    rooms.forEach(r ->
                            System.out.println(
                                    r.getRoomId() + " " +
                                    r.getRoomType() + " ₹" +
                                    r.getPricePerDay()
                            ));
                }

                case 2 -> {
                    sc.nextLine();
                    System.out.print("Name: ");
                    String name = sc.nextLine();
                    System.out.print("Phone: ");
                    String phone = sc.nextLine();
                    System.out.print("Room ID: ");
                    int roomId = sc.nextInt();
                    System.out.print("Check-in: ");
                    LocalDate in = LocalDate.parse(sc.next());
                    System.out.print("Check-out: ");
                    LocalDate out = LocalDate.parse(sc.next());

                    System.out.println(
                            service.bookRoom(name, phone, roomId, in, out)
                                    ? "Booking Successful"
                                    : "Booking Failed"
                    );
                }

                case 3 -> {
                    System.out.print("Booking ID: ");
                    System.out.println(
                            service.cancelBooking(sc.nextInt())
                                    ? "Cancelled"
                                    : "Invalid ID"
                    );
                }

                case 4 -> {
                    System.out.print("Booking ID: ");
                    var b = service.viewBill(sc.nextInt());
                    if (b != null)
                        System.out.println("Total Bill = ₹" + b.getTotalAmount());
                    else
                        System.out.println("Not found");
                }

                case 5 -> System.exit(0);
            }
        }
    }
}
