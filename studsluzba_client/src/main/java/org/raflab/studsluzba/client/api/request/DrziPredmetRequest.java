package org.raflab.studsluzba.client.api.request;

import lombok.Data;
import org.raflab.studsluzba.controllers.request.DrziPredmetNewRequest;

import java.util.List;

@Data
public class DrziPredmetRequest {

    List<DrziPredmetNewRequest> drziPredmet;
    List<DrziPredmetNewRequest> newDrziPredmet;
}
