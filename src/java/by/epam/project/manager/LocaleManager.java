/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package by.epam.project.manager;

import java.util.Locale;

/**
 *
 * @author User
 */
public abstract class LocaleManager {
    
    public static Locale getLocale(String language) {
        return getLocale(language, language);       
    }

    public static Locale getLocale(String language, String country) {
        if (null == country || country.isEmpty() || null == language || language.isEmpty()) {
            return null;
        } else {
            return new Locale(language, country);
        }
    }
    
    
}
