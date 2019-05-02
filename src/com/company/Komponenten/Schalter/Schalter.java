package com.company.Komponenten.Schalter;

import io.javalin.Context;

//added this

public abstract class Schalter {
    protected String Name = ""; //Name des Sensors für die Darstellung auf der Website. Der Name dient dabei als Primärschlüssl d.h. dieser muss eindeutig sein!
    protected String Tabelle = ""; //Name des Tabellenblattes fuer Logging.
    protected boolean logging = false; //Mitloggen von Ein- und Ausschalten!

    public String getName() {
        return Name;
    }

    public String getTabelle() {
        return Tabelle;
    }

    public boolean isLogging() {
        return logging;
    }

    public void calledWithGet(Context ctx){

    }
}
