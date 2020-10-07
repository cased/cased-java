package com.cased;

import com.cased.http.Requestor;


public class Cased {
    Event Event;
    Export Export;

    public Cased() {
        Requestor requestor = new Requestor();

        this.Event = new Event(requestor);
        this.Export = new Export(requestor);
    }
}