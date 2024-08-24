import React from 'react';
import { useLocation } from 'react-router-dom';
import Ticket from './Ticket';

function OrderConfirmationPage() {
  const { state } = useLocation();
  const { order, event } = state;

  return (
    <div className="order-confirmation-page">
      <div className="confirmation-header">
        <span role="img" aria-label="checkmark" className="checkmark">✅</span>
        <h2>Comanda #{order.id} a fost plasată cu succes!</h2>
      </div>
      
      <div className="event-header">
        <img src={`/imgs/${event.banner || 'default-banner.jpg'}`} alt={event.title || 'Event'} className="event-banner-mini" />
        <h1>{event.title || 'Unknown Event'}</h1>
      </div>
      
      <div className="tickets-grid">
        {order.tickets.map(ticket => (
          <Ticket key={ticket.id} ticket={ticket} event={event}/>
        ))}
      </div>
    </div>
  );
}

export default OrderConfirmationPage;
