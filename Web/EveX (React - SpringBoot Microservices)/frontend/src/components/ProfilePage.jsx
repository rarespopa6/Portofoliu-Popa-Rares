import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import QRCode from 'qrcode.react'; // Folosim QRCode pentru a genera codul QR

function ProfilePage() {
  const [activeTickets, setActiveTickets] = useState([]);
  const [pastTickets, setPastTickets] = useState([]);
  const [userInfo, setUserInfo] = useState({});
  const [eventDetails, setEventDetails] = useState({});
  const [error, setError] = useState('');

  const navigate = useNavigate();

  useEffect(() => {
    if (!localStorage.getItem("token")) {
      navigate("/login");
      return;
    }

    const fetchUserProfile = async () => {
      try {
        const token = localStorage.getItem('token');

        // Obține informațiile despre utilizator
        const profileResponse = await fetch('http://localhost:8765/user-service/api/users/profile', {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (profileResponse.ok) {
          const profileData = await profileResponse.json();
          setUserInfo({
            id: profileData.id,
            email: profileData.email,
            role: profileData.role
          });

          // Obține biletele pentru utilizator
          const ticketsResponse = await fetch(`http://localhost:8765/order-service/api/orders/user/${profileData.email}`, {
            method: 'GET',
            headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json'
            }
          });

          if (ticketsResponse.ok) {
            const ticketsData = await ticketsResponse.json();
            const active = ticketsData.filter(order => order.status === 'PENDING');
            const past = ticketsData.filter(order => order.status === 'CANCELLED');
            setActiveTickets(active);
            setPastTickets(past);

            // Obține detalii despre evenimentele asociate cu biletele
            const eventIds = new Set();
            ticketsData.forEach(order => {
              order.tickets.forEach(ticket => {
                eventIds.add(ticket.eventId);
              });
            });

            const eventResponses = await Promise.all(
              Array.from(eventIds).map(id =>
                fetch(`http://localhost:8765/event-service/api/events/${id}`, {
                  method: 'GET',
                  headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                  }
                })
              )
            );

            const events = await Promise.all(eventResponses.map(response => response.json()));
            const eventMap = events.reduce((acc, event) => {
              acc[event.id] = event;
              return acc;
            }, {});

            setEventDetails(eventMap);
          } else {
            console.error('Failed to fetch tickets');
            setError('Failed to fetch tickets');
          }
        } else {
          console.error('Failed to fetch user profile');
          setError('Failed to fetch user profile');
        }
      } catch (error) {
        console.error('Error fetching user profile:', error);
        setError('Error fetching user profile');
      }
    };

    fetchUserProfile();
  }, [navigate]);

  return (
    <div className="profile-page">
      {error && <div className="error-message">{error}</div>}
      <div className="profile-header">
        <h1>Profil Utilizator</h1>
        <div className="profile-info">
          <div className="profile-details">
            <h2>ID Utilizator: {userInfo.id}</h2>
            <p>Email: {userInfo.email}</p>
            <p>Role: {userInfo.role}</p>
            {userInfo.role === "USER" && <button onClick={() => navigate("/become-organizer")}>Become an Organizer</button>}
            {(userInfo.role === "ORGANIZER" || userInfo.role === "ADMIN") && <Link to="/organizer">Organizer Page</Link>}
          </div>
        </div>
      </div>

      <div className="wallet">
        <div className="wallet-section">
          <h2>Bilete Active</h2>
          <div className="ticket-list">
            {activeTickets.length > 0 ? (
              activeTickets.map(order => (
                <div key={order.id} className="order-card">
                  <h3 className="order-title">Comanda numarul #{order.id}</h3>
                  {order.tickets.map(ticket => (
                    <Ticket key={ticket.id} ticket={ticket} event={eventDetails[ticket.eventId]} />
                  ))}
                  <hr style={{border: '2px solid lightblue'}} />
                </div>
              ))
            ) : (
              <p>No active tickets</p>
            )}
          </div>
        </div>

        <div className="wallet-section">
          <h2>Bilete Anulate</h2>
          <div className="ticket-list">
            {pastTickets.length > 0 ? (
              pastTickets.map(order => (
                <div key={order.id} className="order-card">
                  <h3 className="order-title">Comanda {order.id}</h3>
                  {order.tickets.map(ticket => (
                    <Ticket key={ticket.id} ticket={ticket} event={eventDetails[ticket.eventId]} />
                  ))}
                </div>
              ))
            ) : (
              <p>No past tickets</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

const Ticket = ({ ticket, event }) => (
  <div className="ticket-detail">
    <h4>{event?.title || 'Unknown Event'}</h4>
    <img src={`/imgs/${event?.banner || 'default-banner.jpg'}`} alt={event?.title || 'Event'} className="event-banner" />
    <p>Categoria: {ticket.ticketCategory}</p>
    <p>Nume: {ticket.fullName}</p>
    <QRCode value={ticket.qrCode} className="qr-code" />
  </div>
);

export default ProfilePage;
