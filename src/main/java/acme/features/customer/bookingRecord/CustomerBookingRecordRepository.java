
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Bookings.Booking;
import acme.entities.Bookings.BookingRecord;
import acme.entities.Passengers.Passenger;

@Repository
public interface CustomerBookingRecordRepository extends AbstractRepository {

	@Query("SELECT br FROM BookingRecord br WHERE br.booking.id = :bookingId AND br.passenger.id = :passengerId")
	BookingRecord findBookingRecordBybookingIdpassengerId(@Param("bookingId") Integer bookingId, @Param("passengerId") Integer passengerId);

	@Query("SELECT br FROM BookingRecord br WHERE br.id = :bookingRecordId")
	BookingRecord findBookingRecordById(@Param("bookingRecordId") Integer bookingRecordId);

	@Query("SELECT br FROM BookingRecord br WHERE br.booking.id = :bookingId")
	Collection<BookingRecord> findBookingRecordByBookingId(@Param("bookingId") Integer bookingId);

	@Query("SELECT br FROM BookingRecord br WHERE br.passenger.id = :passengerId")
	Collection<BookingRecord> findBookingRecordByPassengerId(@Param("passengerId") Integer passengerId);

	@Query("select p from Passenger p where p.customer.userAccount.id = :customerId")
	Collection<Passenger> findAllPassengersByCustomerId(@Param("customerId") int customerId);

	@Query("select b from Booking b where b.customer.userAccount.id = :customerId and b.draftMode = true")
	Collection<Booking> findAllDraftBookingsByCustomerId(@Param("customerId") int customerId);

}
