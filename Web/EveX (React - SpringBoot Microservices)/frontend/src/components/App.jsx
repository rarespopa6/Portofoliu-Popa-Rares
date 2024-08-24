import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, useLocation } from "react-router-dom";
import { ThemeProvider } from 'styled-components';
import { darkTheme } from '../themes';
import { GlobalStyles } from './GlobalStyles';
import Header from './Header';
import Register from "./Register";
import Login from "./Login";
import EventCarousel from "./EventCarousel";
import EventPage from "./EventPage";
import ProfilePage from "./ProfilePage";
import OrganizerPage from "./OrganizerPage";
import HomePage from "./HomePage";
import Footer from "./Footer";
import OrderDetailsPage from "./OrderDetailsPage";
import OrderConfirmationPage from "./OrderConfirmationPage";
import AllEventsPage from "./AllEventsPage";
import BecomeOrganizerPage from "./BecomeOrganizerPage";

function App(){
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            // Aici poți adăuga logica suplimentară pentru a verifica validitatea token-ului
            setIsAuthenticated(true);
        }
    }, []);

    const handleLogout = () => {
        // Logica de logout
        localStorage.removeItem("token");
        setIsAuthenticated(false);
    };

    return (
        <ThemeProvider theme={darkTheme}>
            <GlobalStyles />
            <Router>
                <div className="App">
                    <Header isAuthenticated={isAuthenticated} onLogout={handleLogout} />
                    <div id="content">
                        <Routes>
                            <Route path="/" element={<><EventCarousel /> <HomePage /> </>} /> 
                            <Route path="/register" element={<Register />} />
                            <Route path="/login" element={<Login />} />
                            <Route path="/event/:id" element={<EventPage />}/>
                            <Route path="/profile" element={<ProfilePage />} />
                            <Route path="/organizer" element={<OrganizerPage />} />
                            <Route path="/order-details" element={<OrderDetailsPage />} />
                            <Route path="/order-confirmation" element={<OrderConfirmationPage />} />
                            <Route path="/all" element={<AllEventsPage />} />
                            <Route path="/become-organizer" element={<BecomeOrganizerPage />} />
                        </Routes>
                    </div>
                </div>
                <Footer />
            </Router>
        </ThemeProvider>
    );
}

export default App;