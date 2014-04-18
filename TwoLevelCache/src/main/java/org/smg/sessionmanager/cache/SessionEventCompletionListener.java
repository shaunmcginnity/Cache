package org.smg.sessionmanager.cache;

import org.smg.sessionmanager.events.SessionEvent;

public interface SessionEventCompletionListener {

	void completed(boolean status, SessionEvent sessionEvent, Session session);

}
