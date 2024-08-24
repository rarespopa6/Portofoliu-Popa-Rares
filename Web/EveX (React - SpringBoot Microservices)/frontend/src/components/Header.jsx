import React from 'react';
import { Link } from 'react-router-dom';
import styled from 'styled-components';
import { FaRegUserCircle } from "react-icons/fa";
import { MdLogout } from "react-icons/md";
import { SiWebmoney } from "react-icons/si";

const HeaderContainer = styled.header`
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 20px;
    background-color: ${({ theme }) => theme.headerBackground};
    color: ${({ theme }) => theme.headerText};
`;

const Logo = styled.div`
    font-size: 24px;
    font-weight: bold;

    a {
        text-decoration: none;
        color: ${({ theme }) => theme.headerText};
    }
`;

const Nav = styled.nav`
    display: flex;
    align-items: center;
`;

const NavLink = styled(Link)`
    margin-left: 15px;
    font-size: 16px;
    color: ${({ theme }) => theme.headerText};

    &:hover {
        color: ${({ theme }) => theme.text};
    }
`;

const CenterNav = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    flex: 1;
    margin: 0 20px;
`;

const CenterNavLink = styled(Link)`
    margin: 0 10px;
    padding-left: 20px;
    padding-right: 20px;
    font-size: 16px;
    color: ${({ theme }) => theme.headerText};
    text-decoration: none;

    &:hover {
        color: ${({ theme }) => theme.text};
    }
`;

const Header = ({ isAuthenticated, onLogout }) => {
    const scrollToElement = (id) => {
        const element = document.getElementById(id);
        if (element) {
            element.scrollIntoView({ behavior: 'smooth' });
        }
    };
    
    return (
        <HeaderContainer>
            <Logo>
                <Link to="/"><SiWebmoney /> <span style={{fontSize: '27px'}}>EveX</span></Link>
            </Logo>
            <CenterNav>
                <CenterNavLink to="/all">Toate evenimentele</CenterNavLink>
                <CenterNavLink href="#search-bar-home" onClick={() => scrollToElement('search-bar-home')}>Cauta eveniment</CenterNavLink>
                <CenterNavLink to="/become-organizer">Devino organizator</CenterNavLink>
            </CenterNav>
            <Nav>
                {isAuthenticated ? (
                    <>
                        <NavLink to="/profile" className="logo-emoji"><FaRegUserCircle style={{color: 'white', fontSize: '23px'}}/></NavLink>
                        <button onClick={onLogout}><MdLogout /></button>
                    </>
                ) : (
                    <>
                        <NavLink to="/login">Login</NavLink>
                        <NavLink to="/register">Register</NavLink>
                    </>
                )}
            </Nav>
        </HeaderContainer>
    );
};

export default Header;
