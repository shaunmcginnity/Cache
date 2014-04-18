package com.openwave.sessionmanager.cache;

import com.openwave.sessionmanager.events.SessionEvent;

public interface SessionEventCompletionListener {

	void completed(boolean status, SessionEvent sessionEvent, Session session);

}
