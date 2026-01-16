package org.raflab.studsluzba.client.navigation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Stack;

@Component
public class NavigationHistory {

    private final Stack<NavigationEvent> backStack = new Stack<>();
    private final Stack<NavigationEvent> forwardStack = new Stack<>();

    @Value("${navigation.max.history:10}")
    private int maxHistory;

    /**
     * Dodaje novu stranicu u istoriju
     */
    public void push(NavigationEvent event) {
        backStack.push(event);
        forwardStack.clear();
        enforceMaxHistory();
    }

    /**
     * Vraća prethodnu stranicu (Back)
     */
    public NavigationEvent goBack() {
        if (backStack.size() <= 1) {
            return null;
        }

        NavigationEvent current = backStack.pop();
        forwardStack.push(current);

        return backStack.peek();
    }

    /**
     * Ide na sledeću stranicu (Forward)
     */
    public NavigationEvent goForward() {
        if (forwardStack.isEmpty()) {
            return null;
        }

        NavigationEvent next = forwardStack.pop();
        backStack.push(next);

        return next;
    }

    /**
     * Da li može nazad?
     */
    public boolean canGoBack() {
        return backStack.size() > 1;
    }

    /**
     * Da li može napred?
     */
    public boolean canGoForward() {
        return !forwardStack.isEmpty();
    }

    /**
     * Trenutna stranica (bez pop-a)
     */
    public NavigationEvent getCurrent() {
        return backStack.isEmpty() ? null : backStack.peek();
    }

    /**
     * Ograniči veličinu backStack-a
     */
    private void enforceMaxHistory() {
        while (backStack.size() > maxHistory) {
            backStack.remove(0);
        }
    }

    /**
     * Reset za logout ili restart
     */
    public void clear() {
        backStack.clear();
        forwardStack.clear();
    }
}