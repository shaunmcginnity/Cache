package org.smg.TwoLevelCache;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class SessionModeller 
{
	private static final HashMap<Long, Session> cache = new HashMap<> ();
	
	private static final class SessionStop implements Runnable {
		private Session session;

		public SessionStop(Session session) {
			this.session = session;
		}

		@Override
		public void run() {
			//System.out.println("STOP " + session.getId());
			SessionModeller.cache.remove(session.getId());
		}
	}

	private static final class DataStart implements Runnable {
		private final Session session;

		public DataStart(Session session) {
			this.session = session;
		}

		@Override
		public void run() {
			//System.out.println("DATA_START " + session.getId());
		}
	}
    private static final class SessionInitiator implements Runnable {

		private final ScheduledThreadPoolExecutor e;
		private final Random r;

		private SessionInitiator(ScheduledThreadPoolExecutor e, Random r) {
			this.e = e;
			this.r = r;
		}

		@Override
		public void run() {
			Session s = new Session(r.nextLong());
			//System.out.println("START " + s.getId());
			SessionModeller.cache.put(s.getId(), s);
			
			if(r.nextInt(100) < 70) {
				e.schedule(new DataStart(s), r.nextInt(10) + 5, TimeUnit.MILLISECONDS);
			}
			e.schedule(new SessionStop(s), r.nextInt(10)+25, TimeUnit.SECONDS);
		}
	}

	public static void main( String[] args )
    {
        final ScheduledThreadPoolExecutor e = new ScheduledThreadPoolExecutor(1);
        final Random r = new Random();
        e.scheduleAtFixedRate(new SessionInitiator(e, r), 0, 100, TimeUnit.MICROSECONDS);

        e.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				System.out.println("SIZE : " + cache.size() );
			}
        	
        }, 0, 1000, TimeUnit.MILLISECONDS);
}
}
