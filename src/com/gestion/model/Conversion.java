/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestion.model;

import static java.lang.Math.floor;
import static java.lang.Math.round;

/**
 *
 * @author routs
 */
public class Conversion {
    
    public static String convertir(int number) {
    String[] units = { "", "Un", "Deux", "Trois", "Quatre", "Cinq", "Six", "Sept", "Huit", "Neuf", "Dix", "Onze","Douze", "Treize", "Quatorze", "Quinze", "Seize", "Dix-sept", "Dix-huit", "Dix-neuf" };
    String[] tens = { "", "", "Vingt", "Trente", "Quarante", "Cinquante", "Soixante", "Soixante-dix", "Quatre-vingt","Quatre-vingt-dix" };

    if (number == 0) {
        return "Zéro";
    }

    if (number < 0) {
        return "moins " + convertir(Math.abs(number));
    }

    String words = "";

    if ((number / 1000000) > 0) {
        words += convertir(number / 1000000) + " Million ";
        number %= 1000000;
    }

    if ((number / 1000) > 0) {
        words += convertir(number / 1000) + " Mille ";
        number %= 1000;
    }

    if ((number / 100) > 0) {
        if (number / 100 == 1) {
            words += "Cent ";
        } else {
            words += convertir(number / 100) + " Cent ";
        }
        number %= 100;
    }

    if (number > 0) {
        if (number < 20) {
            words += units[number];
        } else {
            words += tens[number / 10];
            if ((number % 10) > 0) {
                words += "-" + units[number % 10];
            }
        }
    }

    return words.trim();
}
    
    
    
}
