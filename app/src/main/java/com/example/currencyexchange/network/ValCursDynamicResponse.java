package com.example.currencyexchange.network;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.List;

@Root(name = "ValCurs", strict = false)
public class ValCursDynamicResponse {
    @ElementList(inline = true, name = "Record")
    public List<Record> records;

    public static class Record {
        @org.simpleframework.xml.Attribute(name = "Date")
        public String date;     // "dd.MM.yyyy"

        @org.simpleframework.xml.Element(name = "Value")
        public String value;    // "82,3637"
    }
}
