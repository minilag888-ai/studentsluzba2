package org.raflab.studsluzba.client.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrziPredmetInitResponse {

    private List<DrziPredmetPoklapanjeInitResponse> potpunoPoklapanje;
    private List<DrziPredmetPoklapanjeInitResponse> delimicnoPoklapanje;
    private List<DrziPredmetBezPoklapanjaInitResponse> bezPoklapanja;
}