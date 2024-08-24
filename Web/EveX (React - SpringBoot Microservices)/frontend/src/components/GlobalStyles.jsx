// src/components/GlobalStyles.js
import { createGlobalStyle } from 'styled-components';

export const GlobalStyles = createGlobalStyle`
  body {
    background: ${({ theme }) => theme.body};
    color: ${({ theme }) => theme.text};
    font-family: 'Arial', sans-serif;
    margin: 0;
    padding: 0;
    transition: all 0.50s linear;
  }

  a {
    color: ${({ theme }) => theme.link};
    text-decoration: none;

    &:hover {
      color: ${({ theme }) => theme.text};
    }
  }

  button {
    background-color: ${({ theme }) => theme.buttonBackground};
    color: ${({ theme }) => theme.buttonText};
    border: none;
    padding: 10px 20px;
    cursor: pointer;
    border-radius: 5px;

    &:hover {
      background-color: ${({ theme }) => theme.headerBackground};
    }
  }
`;
