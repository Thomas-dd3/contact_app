package sample.model;

import javafx.collections.ObservableList;

import java.util.Locale;

public class Country {

    public static String[] getCountries(){
        String[] isoCountries = Locale.getISOCountries();
        String[] nameCountries = new String[isoCountries.length];
        for (int i=0 ; i < isoCountries.length ; i++) {
            Locale locale = new Locale("", isoCountries[i]);
            nameCountries[i] = locale.getDisplayCountry();
        }
        return nameCountries;
    }

}
