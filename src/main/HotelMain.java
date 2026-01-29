package main;

import dto.BookingDTO;
import dto.RoomDTO;
import service.HotelService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class HotelMain {

    private static final Scanner sc = new Scanner(System.in);
    private static final HotelService service = new HotelService();

    // ===== HOTEL UI =====
    private static void welcomeScreen() {
        System.out.println("\n=================================================");
        System.out.println("          WELCOME TO GRAND ROYAL HOTEL  ");
        System.out.println("=================================================");
        System.out.println("      Luxury • Comfort • Excellence");
        System.out.println("=================================================\n");
    }

    private static void showMenu() {
        System.out.println("-------------------------------------------------");
        System.out.println(" 1️  Check Room Availability");
        System.out.println(" 2️  Book a Room");
        System.out.println(" 3️  Cancel Booking");
        System.out.println(" 4️  View Bill");
        System.out.println(" 5️  Exit");
        System.out.println("-------------------------------------------------");
        System.out.print("  Please select an option: ");
    }

    private static void exitMessage() {
        System.out.println("\n===============================================");
        System.out.println("  Thank you for choosing GRAND ROYAL HOTEL");
        System.out.println("  We hope to welcome you again soon! ");
        System.out.println("===============================================\n");
    }

    // ===== MAIN METHOD =====
    public static void main(String[] args) {

        welcomeScreen();

        while (true) {

            showMenu();
            int choice = sc.nextInt();

            switch (choice) {

                // =========================
                case 1 -> {
                    System.out.print(" Enter Check-in Date (yyyy-mm-dd): ");
                    LocalDate in = LocalDate.parse(sc.next());

                    System.out.print(" Enter Check-out Date (yyyy-mm-dd): ");
                    LocalDate out = LocalDate.parse(sc.next());

                    List<RoomDTO> rooms =
                            service.checkAvailability(in, out);

                    if (rooms.isEmpty()) {
                        System.out.println(" No rooms available for selected dates.");
                    } else {
                        System.out.println("\n Available Rooms:");
                        System.out.println("---------------------------------------------");
                        rooms.forEach(r ->
                                System.out.println(
                                        "Room ID: " + r.getRoomId()
                                                + " | Type: " + r.getRoomType()
                                                + " | Price: ₹" + r.getPricePerDay() + "/day"
                                ));
                        System.out.println("---------------------------------------------");
                    }
                }

                // =========================
                case 2 -> {
                    sc.nextLine(); // clear buffer

                    System.out.print(" Customer Name: ");
                    String name = sc.nextLine();

                    System.out.print(" Phone Number: ");
                    String phone = sc.nextLine();

                    System.out.print(" Room ID: ");
                    int roomId = sc.nextInt();

                    System.out.print(" Check-in Date (yyyy-mm-dd): ");
                    LocalDate in = LocalDate.parse(sc.next());

                    System.out.print(" Check-out Date (yyyy-mm-dd): ");
                    LocalDate out = LocalDate.parse(sc.next());

                    boolean booked =
                            service.bookRoom(name, phone, roomId, in, out);

                    if (booked) {
                        System.out.println("\n Booking Successful!");
                        System.out.println(" We look forward to hosting you.\n");
                    } else {
                        System.out.println("\n Booking Failed. Please try again.\n");
                    }
                }

                // =========================
                case 3 -> {
                    System.out.print(" Enter Booking ID to Cancel: ");
                    int id = sc.nextInt();

                    if (service.cancelBooking(id)) {
                        System.out.println(" Booking cancelled successfully.");
                    } else {
                        System.out.println(" Invalid booking ID.");
                    }
                }

                // =========================
                case 4 -> {
                    System.out.print(" Enter Booking ID: ");
                    int id = sc.nextInt();

                    BookingDTO b = service.viewBill(id);

                    if (b != null) {
                        System.out.println("\n================== BILL ==================");
                        System.out.println(" Booking ID   : " + b.getBookingId());
                        System.out.println(" Room ID      : " + b.getRoomId());
                        System.out.println(" Check-in     : " + b.getCheckIn());
                        System.out.println(" Check-out    : " + b.getCheckOut());
                        System.out.println("------------------------------------------");
                        System.out.println("  Total Bill: ₹" + b.getTotalAmount());
                        System.out.println("==========================================\n");
                    } else {
                        System.out.println(" Booking not found.");
                    }
                }

                // =========================
                case 5 -> {
                    exitMessage();
                    System.exit(0);
                }

                default ->
                        System.out.println(" Invalid choice. Please try again.");
            }
        }
    }
}
