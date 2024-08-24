import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { CiCalendarDate } from "react-icons/ci";
import { CiLocationOn } from "react-icons/ci";

function EventPage() {
  const { id } = useParams();
  const [event, setEvent] = useState(null);
  const [quantities, setQuantities] = useState({});
  const [error, setError] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    const fetchEvent = async () => {
      try {
        const response = await fetch(`http://localhost:8765/event-service/api/events/${id}`);
        if (response.ok) {
          const data = await response.json();
          setEvent(data);
          const initialQuantities = data.ticketCategories.reduce((acc, category) => {
            acc[category.id] = 0; 
            return acc;
          }, {});
          setQuantities(initialQuantities);
        } else {
          console.error('Failed to fetch event details');
        }
      } catch (error) {
        console.error('Error fetching event:', error);
      }
    };

    fetchEvent();
  }, [id]);

  const handleQuantityChange = (categoryId, change) => {
    setQuantities(prevQuantities => {
      const newQuantity = prevQuantities[categoryId] + change;
      return {
        ...prevQuantities,
        [categoryId]: Math.max(0, Math.min(5, newQuantity))
      };
    });
  };

  const handleOrderClick = () => {
    if (!localStorage.getItem("token")) {
      navigate("/login");
      return;
    }
  
    const totalTickets = Object.values(quantities).reduce((acc, qty) => acc + qty, 0);
  
    if (totalTickets === 0) {
      setError('Trebuie să selectezi cel puțin un bilet pentru a plasa o comandă.');
    } else {
      setError('');
      const selectedTickets = event.ticketCategories
        .filter(category => quantities[category.id] > 0)
        .map(category => ({
          categoryId: category.id,
          category: category.category,
          quantity: quantities[category.id],
          price: category.price
        }));
  
      navigate('/order-details', { state: { selectedTickets, event } });
    }
  };
  

  if (!event) {
    return <div>Loading...</div>;
  }

  return (
    <div className="event-page">
      <img src={`/imgs/${event.banner}`} alt={event.title} className="event-banner" />
      <div className="event-content">
        <div className="left-section">
          <h1>{event.title}</h1>
          <p className="event-date-location"><CiCalendarDate style={{color: 'white', fontSize: '23px'}}/> {new Date(event.eventDate).toLocaleString('ro-RO', {
                day: '2-digit',
                month: 'long',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            })} | <CiLocationOn style={{color: 'white', fontSize: '23px'}}/> {event.location}</p>
          <p className="event-description">{event.description}</p>
        </div>
        <div className="right-section">
          <h2>Categorii Bilete</h2>
          {event.ticketCategories.map(category => (
            <div key={category.id} className="ticket-category">
              <p className="ticket-type">{category.category}</p>
              <p className="ticket-price">{category.price} RON</p>
              <div className="quantity-selector">
                <button onClick={() => handleQuantityChange(category.id, -1)}>-</button>
                <span>{quantities[category.id]}</span>
                <button onClick={() => handleQuantityChange(category.id, 1)}>+</button>
              </div>
            </div>
          ))}
          {error && <p className="error-message">{error}</p>}
          <button className="order-button" onClick={handleOrderClick}>
            Comandă Bilet
          </button>
        </div>
      </div>
    </div>
  );
}

export default EventPage;
