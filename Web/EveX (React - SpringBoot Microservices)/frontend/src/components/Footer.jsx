import React from 'react';

function Footer(){
  return (
    <footer className="footer">
      <div className="footer-links">
        <a href="/terms-and-conditions">Termeni și Condiții</a>
        <a href="/privacy-policy">Politica de Confidențialitate</a>
        <a href="/contact">Contact</a>
        <a href="/about-us">Despre Noi</a>
      </div>
      <div className="footer-copyright">
        <p>&copy; {new Date().getFullYear()} Platforma EveX. Toate drepturile rezervate.</p>
      </div>
    </footer>
  );
};

export default Footer;
