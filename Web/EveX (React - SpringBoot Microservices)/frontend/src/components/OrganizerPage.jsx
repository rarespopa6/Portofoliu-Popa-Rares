import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const OrganizerPage = () => {
  const [events, setEvents] = useState([]);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [editEventId, setEditEventId] = useState(null);
  const [newEvent, setNewEvent] = useState({
    title: '',
    description: '',
    banner: '',
    eventDate: '',
    location: '',
    ticketCategories: [
      { category: '', numberOfTickets: 0, price: 0.0 }
    ]
  });
  const [userId, setUserId] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserProfile = async () => {
      const token = localStorage.getItem('token');

      const profileResponse = await fetch('http://localhost:8765/user-service/api/users/profile', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (profileResponse.ok) {
        const profileData = await profileResponse.json();
        if (profileData.role !== "ORGANIZER" && profileData.role !== "ADMIN") {
          navigate("/");
          return;
        }
        setUserId(profileData.id);
        fetchEvents(profileData.id);
      }
    };

    const fetchEvents = async (organizerId) => {
      const response = await fetch(`http://localhost:8765/event-service/api/events/organizer/all-events/${organizerId}`);
      if (response.ok) {
        const data = await response.json();
        setEvents(data);
      } else {
        console.error('Failed to fetch events');
      }
    };

    fetchUserProfile();
  }, [navigate]);

  const handleChange = (e) => {
    const { name, value, dataset } = e.target;
    if (name === 'category' || name === 'price' || name === 'numberOfTickets') {
      const index = dataset.index;
      setNewEvent(prevState => {
        const ticketCategories = [...prevState.ticketCategories];
        ticketCategories[index] = { ...ticketCategories[index], [name]: value };
        return { ...prevState, ticketCategories };
      });
    } else {
      setNewEvent(prevState => ({ ...prevState, [name]: value }));
    }
  };

  const handleAddCategory = () => {
    setNewEvent(prevState => ({
      ...prevState,
      ticketCategories: [...prevState.ticketCategories, { category: '', numberOfTickets: 0, price: 0.0 }]
    }));
  };

  const handleRemoveCategory = (index) => {
    setNewEvent(prevState => ({
      ...prevState,
      ticketCategories: prevState.ticketCategories.filter((_, i) => i !== index)
    }));
  };

  const handleCreateOrUpdateEvent = async () => {
    const token = localStorage.getItem('token');

    if (isEditing) {
      // Update existing event
      const response = await fetch('http://localhost:8765/event-service/api/events/update', {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ ...newEvent, id: editEventId, organizerId: userId })
      });

      if (response.ok) {
        const updatedEvent = await response.json();
        setEvents(events.map(event => (event.id === updatedEvent.id ? updatedEvent : event)));
        setIsEditing(false);
        setEditEventId(null);
      } else {
        console.error('Failed to update event');
      }
    } else {
      // Create new event
      const response = await fetch('http://localhost:8765/event-service/api/events/create', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ ...newEvent, organizerId: userId })
      });

      if (response.ok) {
        const createdEvent = await response.json();
        setEvents([...events, createdEvent]);
      } else {
        console.error('Failed to create event');
      }
    }

    setNewEvent({
      title: '',
      description: '',
      banner: '',
      eventDate: '',
      location: '',
      ticketCategories: [{ category: '', numberOfTickets: 0, price: 0.0 }]
    });
    setShowCreateForm(false);
  };

  const handleEditEvent = (event) => {
    setIsEditing(true);
    setEditEventId(event.id);
    setNewEvent({
      title: event.title,
      description: event.description,
      banner: event.banner,
      eventDate: event.eventDate,
      location: event.location,
      ticketCategories: event.ticketCategories
    });
    setShowCreateForm(true);
  };

  const handleDeleteEvent = async (id) => {
    const token = localStorage.getItem('token');

    const response = await fetch(`http://localhost:8765/event-service/api/events/delete/${id}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });

    if (response.ok) {
      setEvents(events.filter(event => event.id !== id));
    } else {
      console.error('Failed to delete event');
    }
  };

  return (
    <div className="organizer-page">
      <div className="organizer-actions">
        <h1>Organizator Page</h1>

        <button onClick={() => {
          setShowCreateForm(!showCreateForm);
          if (showCreateForm) {
            setIsEditing(false);
            setEditEventId(null);
            setNewEvent({
              title: '',
              description: '',
              banner: '',
              eventDate: '',
              location: '',
              ticketCategories: [{ category: '', numberOfTickets: 0, price: 0.0 }]
            });
          }
        }}>
          {showCreateForm ? 'Ascunde Formular' : isEditing ? 'Actualizează Eveniment' : 'Creează Eveniment'}
        </button>
      </div>

      {showCreateForm && (
        <div className="create-event">
          <h2>{isEditing ? 'Actualizează Eveniment' : 'Creează un Eveniment'}</h2>
          <input
            type="text"
            name="title"
            value={newEvent.title}
            onChange={handleChange}
            placeholder="Titlu"
          />
          <textarea
            name="description"
            value={newEvent.description}
            onChange={handleChange}
            placeholder="Descriere"
          />
          <input
            type="text"
            name="banner"
            value={newEvent.banner}
            onChange={handleChange}
            placeholder="Banner URL"
          />
          <input
            type="datetime-local"
            name="eventDate"
            value={newEvent.eventDate}
            onChange={handleChange}
          />
          <input
            type="text"
            name="location"
            value={newEvent.location}
            onChange={handleChange}
            placeholder="Locație"
          />
          
          <div className="ticket-categories">
            <h3>Categorii Bilete</h3>
            {newEvent.ticketCategories.map((category, index) => (
              <div key={index} className="ticket-category">
                <input
                  type="text"
                  name="category"
                  data-index={index}
                  value={category.category}
                  onChange={handleChange}
                  placeholder="Categorie"
                />
                <input
                  type="number"
                  name="numberOfTickets"
                  data-index={index}
                  value={category.numberOfTickets}
                  onChange={handleChange}
                  placeholder="Număr bilete"
                  min="0"
                />
                <input
                  type="number"
                  name="price"
                  data-index={index}
                  value={category.price}
                  onChange={handleChange}
                  placeholder="Preț (RON)"
                  step="0.01"
                  min="0"
                />
                <button type="button" onClick={() => handleRemoveCategory(index)}>Șterge categorie</button>
              </div>
            ))}
            <button type="button" onClick={handleAddCategory}>Adaugă categorie</button>
          </div>
          
          <button onClick={handleCreateOrUpdateEvent}>
            {isEditing ? 'Actualizează Eveniment' : 'Creează Eveniment'}
          </button>
        </div>
      )}

      <div className="events-list">
        <h2>Evenimentele Tale</h2>
        {events.map(event => (
          <div key={event.id} className="event-item">
            <img src={`imgs/${event.banner}`} alt={event.title} />
            <div className="event-info">
              <h3>{event.title}</h3>
              <p>{event.description}</p>
              <p>{new Date(event.eventDate).toLocaleString('ro-RO')}</p>
              <p>{event.location}</p>
              <button onClick={() => handleEditEvent(event)}>Actualizează</button>
              <button onClick={() => handleDeleteEvent(event.id)}>Șterge</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default OrganizerPage;
