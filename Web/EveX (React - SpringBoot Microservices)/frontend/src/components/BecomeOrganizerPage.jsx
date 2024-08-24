import React, { useState } from 'react';

const BecomeOrganizerPage = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    // Handle become organizer logic here
    console.log("Request to become an organizer:", { name, email, message });
  };

  return (
    <div className="become-organizer-page">
      <div className="content-container">
        <div className="form-section">
          <p className="form-title">Devino Organizator</p>
          <p className="form-description">
            Dacă ești interesat să organizezi evenimente pe platforma noastră, completează formularul de mai jos. 
            Așteptăm cu interes propunerea ta!
          </p>
          <form onSubmit={handleSubmit} className="organizer-form">
            <input
              type="text"
              placeholder="Nume complet"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="form-input"
            />
            <input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="form-input"
            />
            <textarea
              placeholder="Mesaj"
              value={message}
              onChange={(e) => setMessage(e.target.value)}
              className="form-textarea"
            />
            <button type="submit" className="form-submit-button">Trimite</button>
          </form>
        </div>
        <div className="regulations-section">
          <p className="regulations-title">Regulament</p>
          <p className="regulations-text">
            1. Evenimentele trebuie să fie relevante și interesante pentru publicul nostru.
            <br />
            2. Organizatatorii trebuie să asigure o experiență pozitivă pentru participanți.
            <br />
            3. Orice schimbare sau anulare a evenimentului trebuie anunțată din timp.
            <br />
            4. Respectarea regulamentului este esențială pentru menținerea unui standard ridicat pe platformă.
          </p>
        </div>
      </div>
    </div>
  );
};

export default BecomeOrganizerPage;
