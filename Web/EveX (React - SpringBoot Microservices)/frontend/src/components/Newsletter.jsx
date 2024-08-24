import React, { useState } from "react";

function Newsletter(){
    const [email, setEmail] = useState('');

  const subscribeToNewsletter = (e) => {
    e.preventDefault();
    // Deocamdată, doar logăm emailul. Într-o aplicație reală, ai trimite acest email la un backend sau un serviciu de email.
    console.log('Email subscribed:', email);
    alert('Mulțumim pentru abonare!');
    setEmail(''); 
  };

  return (
    <div className="newsletter-section">
      <h2>Fii la curent cu cele mai noi evenimente</h2>
      <p>Abonează-te la newsletter-ul nostru pentru a primi notificări despre evenimentele viitoare direct în inbox-ul tău.</p>
      <form className="newsletter-form" onSubmit={subscribeToNewsletter}>
        <input
          type="email"
          placeholder="Introdu adresa ta de email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <button type="submit">Abonează-te</button>
      </form>
    </div>
  );
}

export default Newsletter;