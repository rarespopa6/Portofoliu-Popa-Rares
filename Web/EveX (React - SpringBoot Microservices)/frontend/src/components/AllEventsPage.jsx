// src/pages/AllEventsPage.jsx
import React, { useEffect, useState, useRef } from 'react';
import EventCard from './EventCard';

function AllEventsPage() {
  const [events, setEvents] = useState([]);
  const eventsRef = useRef(null);

  useEffect(() => {
    const fetchEvents = async () => {
      try {
        const response = await fetch('http://localhost:8765/event-service/api/events/all');
        if (response.ok) {
          const data = await response.json();
          setEvents(data);
        } else {
          console.error('Failed to fetch events');
        }
      } catch (error) {
        console.error('Error fetching events:', error);
      }
    };

    fetchEvents();
  }, []);


  return (
    <div className="events-page">
        <h1>Toate evenimentele</h1>
        <div className="events-grid">
          {events.map(event => (
            <EventCard key={event.id} event={event} />
          ))}
        </div>
    </div>
  );
}

export default AllEventsPage;
