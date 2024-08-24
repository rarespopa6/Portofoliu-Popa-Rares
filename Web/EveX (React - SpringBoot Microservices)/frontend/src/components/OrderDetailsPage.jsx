import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { v4 as uuidv4 } from 'uuid';

function OrderDetailsPage() {
  const { state } = useLocation();
  const { selectedTickets, event } = state;
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  // array de bilete cu identificatori unici
  const [ticketDetails, setTicketDetails] = useState(
    selectedTickets.flatMap(ticket =>
      Array(ticket.quantity).fill().map(() => ({
        id: uuidv4(),
        fullName: '',
        category: ticket.category
      }))
    )
  );

  const handleNameChange = (id, name) => {
    setTicketDetails(prevDetails =>
      prevDetails.map(detail =>
        detail.id === id ? { ...detail, fullName: name } : detail
      )
    );
  };

  const handlePlaceOrder = async () => {
    const allFieldsFilled = ticketDetails.every(ticket => ticket.fullName.trim() !== '');

    if (!allFieldsFilled) {
        setErrorMessage('Te rog să completezi toate câmpurile de Nume complet.');
        return;
    }

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
  
      if (!profileResponse.ok) {
        console.error('Failed to fetch user profile');
        return;
      }
  
      const profileData = await profileResponse.json();
      const { email, role } = profileData;
  
      const response = await fetch('http://localhost:8765/order-service/api/orders/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
          user: { email, role },
          tickets: ticketDetails.map(detail => ({
            eventId: event.id,
            fullName: detail.fullName,
            ticketCategory: detail.category
          })),
          orderDate: new Date().toISOString(),
          status: 'PENDING'
        })
      });
  
      if (response.ok) {
        const data = await response.json();
        navigate('/order-confirmation', { state: { order: data, event } });
      } else {
        console.error('Order failed');
      }
    } catch (error) {
      console.error('Error placing order:', error);
    }
  };
  

  return (
    <div className="order-details-page">
      <div className="event-header">
        <img src={`/imgs/${event.banner}`} alt={event.title} className="event-banner-mini" />
        <h1>{event.title}</h1>
      </div>
      <h2>Completează Detaliile Comenzii</h2>
      {ticketDetails.map((ticket, index) => (
        <div key={ticket.id} className="ticket-detail">
          <h3>{ticket.category} - Bilet {index + 1}</h3>
          <input
            type="text"
            placeholder="Nume complet"
            value={ticket.fullName}
            onChange={e => handleNameChange(ticket.id, e.target.value)}
            className="name-input"
          />
        </div>
      ))}
      {errorMessage && <p className="error-message">{errorMessage}</p>}
      <button className="place-order-button" onClick={handlePlaceOrder}>
        Plasează Comanda
      </button>
    </div>
  );
}

export default OrderDetailsPage;
