package org.raflab.studsluzba.client.navigation;

import javafx.scene.Parent;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NavigationEvent {

    private Parent view;           // JavaFX Node (cela forma)
    private String title;           // Naslov stranice
    private Object context;         // Dodatni podaci (npr. studentId)

    public NavigationEvent(Parent view, String title) {
        this.view = view;
        this.title = title;
        this.context = null;
    }
}