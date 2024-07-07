import React, { useState } from 'react';
import { FaFacebook, FaInstagram, FaXTwitter } from "react-icons/fa6";

function Contact() {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    message: ''
  });

  const [formStatus, setFormStatus] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:3001/api/contact', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
      });
      const result = await response.json();
      if (response.ok) {
        setFormStatus('Message sent successfully!');
        setFormData({ name: '', email: '', message: '' });
      } else {
        setFormStatus('Failed to send message. Please try again.');
      }
    } catch (error) {
      console.error('Error submitting contact form:', error);
      setFormStatus('Failed to send message. Please try again.');
    }
  };

  return (
    <div className="contact-container">
      <h2>Contact Us</h2>
      <div className="contact-details">
        <div className="contact-item">
          <h3>Phone</h3>
          <p>+4077725200</p>
        </div>
        <div className="contact-item">
          <h3>Email</h3>
          <p>support@avx.com</p>
          <p>info@avx.com</p>
        </div>
        <div className="contact-item">
          <h3>Address</h3>
          <p>Str. Republicii 52</p>
          <p>Brasov, Romania</p>
        </div>
      </div>
      <div className="contact-hours">
          <h3>Business Hours</h3>
          <p>Monday - Friday: 09:00 - 18:00</p>
          <p>Saturday: 10:00 - 15:00</p>
          <p>Sunday: Closed</p>
        </div>
      {/* <form onSubmit={handleSubmit} className="contact-form">
        <h3>Send us a message</h3>
        <input
          type="text"
          name="name"
          placeholder="Your Name"
          value={formData.name}
          onChange={handleChange}
          required
        />
        <input
          type="email"
          name="email"
          placeholder="Your Email"
          value={formData.email}
          onChange={handleChange}
          required
        />
        <textarea
          name="message"
          placeholder="Your Message"
          value={formData.message}
          onChange={handleChange}
          required
        ></textarea>
        <button type="submit">Send</button>
        {formStatus && <p className="form-status">{formStatus}</p>}
      </form> */}
      <div className="contact-map">
        <iframe className="map"
          src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d87189.13338908623!2d25.58013612803511!3d45.657974065132396!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x40b25fc7b6c5e985%3A0x2054d1f41646c44e!2sBra%C8%99ov%2C%20Romania!5e0!3m2!1sen!2sus!4v1625587494681!5m2!1sen!2sus"
          width="725"
          height="450"
          style={{ border: 0 }}
          allowFullScreen=""
          loading="lazy"
          title="Company Location"
        ></iframe>
      </div>
      <div className="contact-social-media">
        <h3>Follow us on</h3>
        <a href="https://www.facebook.com" target="_blank" rel="noopener noreferrer"><FaFacebook /></a>
        <a href="https://www.instagram.com" target="_blank" rel="noopener noreferrer"><FaInstagram /></a>
        <a href="https://www.twitter.com" target="_blank" rel="noopener noreferrer"><FaXTwitter /></a>
      </div>
    </div>
  );
}

export default Contact;
