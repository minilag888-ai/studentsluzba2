package org.raflab.studsluzba.client.navigation;

import javafx.scene.Parent;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NavigationEvent {
    private Parent view;
    private String title;
    private Object context;  // viewType (studenti, ispiti, predmeti)

    public NavigationEvent(Parent view, String title) {
        this.view = view;
        this.title = title;
        this.context = null;
    }

    public String getViewType() {
        return context != null ? context.toString() : "studenti";
    }
}