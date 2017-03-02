package fi.linuxbox.cdi.swing.core.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Base class for Swing actions that fire a CDI event (asynchronously).
 *
 * @param <T> Type of the CDI event
 */
abstract public class AbstractCdiAction<T> extends AbstractAction {
    private static final Logger log = LoggerFactory.getLogger(AbstractCdiAction.class);

    @Inject
    private Event<T> event;
    @Inject
    private T command;

    protected AbstractCdiAction(final String name, final String actionCommand) {
        super(name);
        putValue(Action.ACTION_COMMAND_KEY, actionCommand);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        log.info("starting swing-action: $s", e.getActionCommand());
        event.fireAsync(command);
        log.info("finished swing-action: $s", e.getActionCommand());
    }
}
