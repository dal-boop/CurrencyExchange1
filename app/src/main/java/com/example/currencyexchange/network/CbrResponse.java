package com.example.currencyexchange.network;

import com.example.currencyexchange.models.CurrencyCbr;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "ValCurs", strict = false)
public class CbrResponse {
    @ElementList(inline = true, name = "Valute")
    public List<CurrencyCbr> valutes;
}
