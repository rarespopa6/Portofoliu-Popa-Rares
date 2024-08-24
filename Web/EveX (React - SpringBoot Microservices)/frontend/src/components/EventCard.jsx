import React from 'react';
import { Link } from 'react-router-dom';

const EventCard = ({ event }) => {
  return (
    <div className="event-card">
      <img src={`imgs/${event.banner}`} alt={event.title} className="event-banner" />
      <div className="event-details">
        <div className="event-details-content">
            <h3>{event.title}</h3>
        </div>
        <div className="event-details-content">
            <p>{new Date(event.eventDate).toLocaleString('ro-RO')}</p>
        </div>
        <div className="event-details-content" style={{marginTop: '-10px'}}>
            <p>{event.location}</p>
        </div>
        <div className="event-details-link">
            <Link to={`/event/${event.id}`} className="event-link">Detalii</Link>
        </div>
      </div>
    </div>
  );
};

export default EventCard;
