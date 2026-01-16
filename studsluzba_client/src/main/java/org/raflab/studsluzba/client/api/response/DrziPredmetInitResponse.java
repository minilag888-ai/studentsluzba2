package org.raflab.studsluzba.client.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.raflab.studsluzba.controllers.response.DrziPredmetBezPoklapanjaInitResponse;

import java.util.List;

@Data
@AllArgsConstructor
public class DrziPredmetInitResponse {
    private List<DrziPredmetPoklapanjeInitResponse> potpunoPoklapanje;
    private List<DrziPredmetPoklapanjeInitResponse> delimicnoPoklapanje;
    private List<DrziPredmetBezPoklapanjaInitResponse> bezPoklapanja;
}