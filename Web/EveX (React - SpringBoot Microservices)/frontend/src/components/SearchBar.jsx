import React, { useState, useEffect } from 'react';
import EventCard from './EventCard';
import { FaSearch } from "react-icons/fa";

function SearchBar(){
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [suggestions, setSuggestions] = useState([]);
  const [searchType, setSearchType] = useState('title'); 
  const [noResults, setNoResults] = useState(false);

  useEffect(() => {
    if (query.length > 1) {
      fetchSuggestions();
    } else {
      setSuggestions([]);
    }
  }, [query]);

  const fetchSuggestions = async () => {
    const response = await fetch(`http://localhost:8765/event-service/api/events/${searchType}/${query}`);
    if (response.ok) {
      const data = await response.json();
      setSuggestions(data);
    } else {
      setSuggestions([]);
    }
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    const response = await fetch(`http://localhost:8765/event-service/api/events/${searchType}/${query}`);
    if (response.ok) {
      const data = await response.json();
      setResults(data);
      setNoResults(data.length === 0);
    } else {
      setResults([]);
      setNoResults(true);
    }
    setSuggestions([]);
  };

  return (
    <div className="search-bar-container">
      <div className="search-title">
        <p className='info-title' style={{fontSize: '40px', color: 'black'}}>Caută evenimente <span style={{color: 'black', marginLeft: '10px'}}><FaSearch /></span></p>
      </div>
      <div className="search-bar">
        <form onSubmit={handleSearch}>
          <select value={searchType} onChange={(e) => setSearchType(e.target.value)}>
            <option value="title">Titlu</option>
            <option value="location">Locație</option>
          </select>
          <input className="search-input"
            type="text"
            placeholder={`Căutare după ${searchType}`}
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
          <button type="submit">Caută</button>
          {suggestions.length > 0 && (
            <ul className="suggestions">
              {suggestions.map((event) => (
                <li key={event.id} onClick={() => setQuery(event[searchType])}>
                  {event[searchType]}
                </li>
              ))}
            </ul>
          )}
        </form>
      </div>
      <div className="search-results">
        {results.length > 0 ? (
          <div className="grid-container">
            {results.map((event) => <EventCard key={event.id} event={event} />)}
          </div>
        ) : (
          noResults && <p>Nu există rezultate pentru căutarea ta.</p>
        )}
      </div>
    </div>
  );
  
};

export default SearchBar;
