package com.example.currencyexchange.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Valute", strict = false)
public class CurrencyCbr {
    @Attribute(name = "ID")
    public String id;

    @Element(name = "CharCode")
    public String charCode;

    @Element(name = "Value")
    public String value;
    @Element(name = "Nominal")   public int nominal;

}

