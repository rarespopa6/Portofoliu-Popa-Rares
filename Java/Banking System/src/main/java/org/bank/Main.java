package org.bank;


import org.bank.ui.UserInterface;

public class Main {
    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        ui.start();

        // TODO Login Credentials
        // pentru In-Memory:
        // "darius@gmail.com", "1234");
        // "rares@gmail.com", "1234");
        // "jane@gmail.com", "1234");

        // pentru File:
        // "raul@gmail.com", "1234"
        // "darius@gmail.com", "1234"
        // "rares@gmail.com", "1234"
    }
}