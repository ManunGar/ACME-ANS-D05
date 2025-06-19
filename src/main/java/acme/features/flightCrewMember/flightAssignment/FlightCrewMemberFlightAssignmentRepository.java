
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.ActivityLogs.ActivityLog;
import acme.entities.FlightAssignments.Duty;
import acme.entities.FlightAssignments.FlightAssignment;
import acme.entities.Legs.Legs;
import acme.realms.AvailabilityStatus;
import acme.realms.FlightCrewMember;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa WHERE fa.leg.arrival < :currentMoment")
	Collection<FlightAssignment> findCompletedAssignments(Date currentMoment);

	@Query("select fa from FlightAssignment fa WHERE fa.leg.arrival > :currentMoment")
	Collection<FlightAssignment> findNotCompletedAssignments(Date currentMoment);

	@Query("select fa from FlightAssignment fa where fa.id = :flightAssignmentId")
	FlightAssignment findFlightAssignmentById(int flightAssignmentId);

	@Query("select fa from FlightAssignment fa where fa.leg.id in (select l.id from Legs l where l.flight.id = :flightId)")
	Collection<FlightAssignment> findAssignmentsByFlightId(int flightId);

	@Query("select l from Legs l")
	List<Legs> findAllLegs();

	@Query("select fcm from FlightCrewMember fcm WHERE fcm.availabilityStatus = :availabilityStatus")
	List<FlightCrewMember> findFlightCrewMembersByAvailabilityStatus(AvailabilityStatus availabilityStatus);

	@Query("select fa from FlightAssignment fa WHERE fa.flightCrewMember.id = :flightCrewMemberId")
	Collection<FlightAssignment> findFlightAssignmentByFlightCrewMember(int flightCrewMemberId);

	@Query("select fcm from FlightCrewMember fcm WHERE fcm.id = :flightCrewMemberId")
	FlightCrewMember findFlightCrewMembersById(int flightCrewMemberId);

	@Query("select l from Legs l where l.id = :legId")
	Legs findLegById(int legId);

	@Query("select count(fa) > 0 from FlightAssignment fa WHERE fa.leg.id = :legId AND fa.duty = :duty")
	boolean existsFlightCrewMemberWithDutyInLeg(int legId, Duty duty);

	@Query("select al from ActivityLog al where al.flightAssignment.id = :flightAssignmentId")
	Collection<ActivityLog> findActivityLogsByFlightAssignamentId(int flightAssignmentId);

	@Query("select fa from FlightAssignment fa where fa.leg.arrival < :currentMoment and fa.flightCrewMember.id = :flighCrewMemberId")
	Collection<FlightAssignment> legCompletedByFlightAssignmentId(int flighCrewMemberId, Date currentMoment);

	//	@Query("select b from FlightAssignment b WHERE b.id = :bookingId")
	//	Booking findBookingById(@Param("bookingId") int bookingId);
	//
	//	@Query("select b from FlightAssignment b WHERE b.locatorCode = :locatorCode")
	//	Booking findBookingByLocatorCode(@Param("locatorCode") String locatorCode);
	//
	//	@Query("select bk.passenger from BookingRecord bk where bk.booking.id = :bookingId")
	//	Collection<Passenger> findPassengersByBooking(@Param("bookingId") Integer bookingId);
	//
	//	@Query("select bk from Booking bk where bk.customer.userAccount.id = :customerId")
	//	Collection<Booking> findBookingByCustomer(@Param("customerId") Integer customerId);
	//
	//	@Query("SELECT bk.customer FROM Booking bk WHERE bk.id = :bookingId")
	//	Customer findCustomerByBooking(@Param("bookingId") Integer bookingId);
	//
	//	@Query("select c from Customer c where c.id = :customerId")
	//	Customer findCustomerById(@Param("customerId") Integer customerId);
	//
	//	@Query("select br from BookingRecord br where br.booking.id = :bookingId")
	//	Collection<BookingRecord> findBookingRecordByBooking(@Param("bookingId") int bookingId);

}
