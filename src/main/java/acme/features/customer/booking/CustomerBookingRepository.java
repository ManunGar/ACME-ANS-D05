
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Bookings.Booking;
import acme.entities.Bookings.BookingRecord;
import acme.entities.Flight.Flight;
import acme.entities.Passengers.Passenger;
import acme.realms.Customer;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b")
	Collection<Booking> findAllBooking();

	@Query("select b from Booking b WHERE b.draftMode = false")
	Collection<Booking> findAllBookingPublished();

	@Query("select b from Booking b WHERE b.id = :bookingId")
	Booking findBookingById(@Param("bookingId") int bookingId);

	@Query("select b from Booking b WHERE b.locatorCode = :locatorCode")
	Booking findBookingByLocatorCode(@Param("locatorCode") String locatorCode);

	@Query("select bk.passenger from BookingRecord bk where bk.booking.id = :bookingId")
	Collection<Passenger> findPassengersByBooking(@Param("bookingId") Integer bookingId);

	@Query("select bk.passenger.fullName from BookingRecord bk where bk.booking.id = :bookingId")
	Collection<String> findPassengersNameByBooking(@Param("bookingId") Integer bookingId);

	@Query("select bk from Booking bk where bk.customer.userAccount.id = :customerId")
	Collection<Booking> findBookingByCustomer(@Param("customerId") Integer customerId);

	@Query("SELECT bk.customer FROM Booking bk WHERE bk.id = :bookingId")
	Customer findCustomerByBooking(@Param("bookingId") Integer bookingId);

	@Query("select c from Customer c where c.id = :customerId")
	Customer findCustomerById(@Param("customerId") Integer customerId);

	@Query("select br from BookingRecord br where br.booking.id = :bookingId")
	Collection<BookingRecord> findBookingRecordByBooking(@Param("bookingId") int bookingId);

	@Query("select ffrom Flight fwhere f.draftMode = false")
	Collection<Flight> findAllPublishedFlights();

}
