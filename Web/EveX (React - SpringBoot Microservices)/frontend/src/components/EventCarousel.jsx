import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Carousel } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css'; 

function EventCarousel() {
  const [events, setEvents] = useState([]);

  useEffect(() => {
    const fetchEvents = async () => {
      try {
        const response = await fetch('http://localhost:8765/event-service/api/events/all');
        if (response.ok) {
          const data = await response.json();
          setEvents(data.slice(0, 5)); // Preluăm doar primele 5 evenimente
        } else {
          console.error('Failed to fetch events');
        }
      } catch (error) {
        console.error('Error fetching events:', error);
      }
    };

    fetchEvents();
  }, []);

  const formatEventDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('ro-RO', options);
  };

  return (
    <div className="carousel-container">
      <Carousel className="custom-carousel">
        {events.map((event) => (
          <Carousel.Item key={event.id}>
            <img
              className="d-block w-100 custom-image"
              src={`/imgs/${event.banner}`}
              alt={event.title}
            />
            <div className="mid-content">
           <Carousel.Caption className="custom-carousel-item">
            <div>
                <p className="carousel-title" style={{fontSize: '3rem'}}>{event.title}</p>
                <p className="carousel-loc">{event.location}</p>
                {/* <p className="carousel-loc">{formatEventDate(event.eventDate)}</p> */}
                </div>
              <Link to={`/event/${event.id}`} className="carousel-loc" style={{fontSize: '2rem'}}>Cumpără Bilet</Link>
            </Carousel.Caption>
            </div>
          </Carousel.Item>
        ))}
      </Carousel>
    </div>
  );
}

export default EventCarousel;
