package org.raflab.studsluzba.controllers.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class OsvojeniPoeniRequest {

    @NotNull
    private Long studentIndeksId;

    @NotNull
    private Long predispitnaObavezaId;

    @NotNull
    private Integer poeni;
}