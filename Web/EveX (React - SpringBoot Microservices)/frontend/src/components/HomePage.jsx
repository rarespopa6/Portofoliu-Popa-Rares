import React, { useEffect, useState, useRef } from 'react';
import EventCard from './EventCard';
import { IoStarSharp } from "react-icons/io5";
import { CiCalendarDate } from "react-icons/ci";
import { FaRegCalendarPlus } from "react-icons/fa";
import Newsletter from './Newsletter';
import SearchBar from './SearchBar';

const HomePage = () => {
  const [recommendedEvents, setRecommendedEvents] = useState([]);
  const [thisWeekEvents, setThisWeekEvents] = useState([]);
  const [newEvents, setNewEvents] = useState([]);
  const [showRecommendedButtons, setShowRecommendedButtons] = useState(false);
  const [showWeekButtons, setShowWeekButtons] = useState(false);
  const [showNewEventsButtons, setShowNewEventsButtons] = useState(false);
  const recommendedRef = useRef(null);
  const weekRef = useRef(null);
  const newEventsRef = useRef(null);

  useEffect(() => {
        const fetchRecommendedEvents = async () => {
        const response = await fetch('http://localhost:8765/event-service/api/events/recommended');
        if (response.ok) {
            const data = await response.json();
            setRecommendedEvents(data);
        } else {
            console.error('Failed to fetch recommended events');
        }
        };

        const fetchThisWeekEvents = async () => {
          const endDate = new Date();
          endDate.setDate(endDate.getDate() + 7);
          const formattedDate = endDate.toISOString();

          const response = await fetch(`http://localhost:8765/event-service/api/events/before-date?date=${formattedDate}`);
          if (response.ok) {
              const data = await response.json();
              setThisWeekEvents(data);
          } else {
              console.error('Failed to fetch events for this week');
          }
        };

        const fetchNewEvents = async () => {
          const response = await fetch('http://localhost:8765/event-service/api/events/all');
          if (response.ok) {
            const data = await response.json();
            setNewEvents(data.slice(-5).reverse());  // Ultimele 4 evenimente
          } else {
            console.error('Failed to fetch new events');
          }
        };

        fetchRecommendedEvents();
        fetchThisWeekEvents();
        fetchNewEvents();
    }, []);

    useEffect(() => {
        const checkScrollButtons = (ref, setShowButtons) => {
          const container = ref.current;
          if (container) {
            const list = container.querySelector('.event-list');
            if (list) {
              if (list.scrollWidth > container.clientWidth) {
                setShowButtons(true);
              } else {
                setShowButtons(false);
              }
            }
          }
        };
    
        checkScrollButtons(recommendedRef, setShowRecommendedButtons);
        checkScrollButtons(weekRef, setShowWeekButtons);
        checkScrollButtons(newEventsRef, setShowNewEventsButtons);

        const handleResize = () => {
          checkScrollButtons(recommendedRef, setShowRecommendedButtons);
          checkScrollButtons(weekRef, setShowWeekButtons);
          checkScrollButtons(newEventsRef, setShowNewEventsButtons);
        };

        window.addEventListener('resize', handleResize);
        handleResize(); // Initial check

        return () => window.removeEventListener('resize', handleResize);
      }, [recommendedEvents, thisWeekEvents, newEvents]);
    
      const scrollLeft = (ref) => {
        ref.current.scrollBy({ left: -200, behavior: 'smooth' });
      };
    
      const scrollRight = (ref) => {
        ref.current.scrollBy({ left: 200, behavior: 'smooth' });
      };
  return (
    <div className="home-page">
      <div className="info-title">
        <p className='info-subtitle'><span><IoStarSharp /></span> Recomandarea noastră</p>
      </div>
      <div className="event-container">
        {showRecommendedButtons && (
          <button className="scroll-button left show" onClick={() => scrollLeft(recommendedRef)}>&lt;</button>
        )}
        <div className="event-list" ref={recommendedRef}>
         {recommendedEvents.map(event => (
            <EventCard key={event.id} event={event} />
          ))}
        </div>
        {showRecommendedButtons && (
          <button className="scroll-button right show" onClick={() => scrollRight(recommendedRef)}>&gt;</button>
        )}
      </div>

      <div className="info-title">
        <p className='info-subtitle'><span><CiCalendarDate /></span> Săptămâna aceasta</p>
      </div>
      <div className="event-container">
        {showWeekButtons && (
          <button className="scroll-button left show" onClick={() => scrollLeft(weekRef)}>&lt;</button>
        )}
        <div className="event-list" ref={weekRef}>
        {thisWeekEvents.map(event => (
            <EventCard key={event.id} event={event} />
          ))}
        </div>
        {showWeekButtons && (
          <button className="scroll-button right show" onClick={() => scrollRight(weekRef)}>&gt;</button>
        )}
      </div>
      <SearchBar id="search-bar-home"/>
      <div id='organizer-promo' className="organizer-promo">
        <h2>Dorești să organizezi evenimente?</h2>
        <button className="organizer-button" onClick={() => window.location.href = "/become-organizer"}>
          Detalii despre cum să devii organizator
        </button>
      </div>

      <div className="info-title">
        <p className='info-subtitle'><span><FaRegCalendarPlus /></span> Evenimente noi</p>
      </div>
      <div className="event-container">
        {showNewEventsButtons && (
          <button className="scroll-button left show" onClick={() => scrollLeft(newEventsRef)}>&lt;</button>
        )}
        <div className="event-list" ref={newEventsRef}>
          {newEvents.map(event => (
            <EventCard key={event.id} event={event} />
          ))}
        </div>
        {showNewEventsButtons && (
          <button className="scroll-button right show" onClick={() => scrollRight(newEventsRef)}>&gt;</button>
        )}
      </div>
      <hr className='line'/>
      <Newsletter />
    </div>
  );
}
export default HomePage;
