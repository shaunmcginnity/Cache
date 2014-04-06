package org.smg.TwoLevelCache;

import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.smg.TwoLevelCache.LevelOneCache.EvictionOrder;

/**
 * Hello world!
 *
 */
public class SessionModeller 
{
	private static final LevelTwoCache l2Cache = new LevelTwoCache ();
	private static final LevelOneCache cache = new LevelOneCache (10000, l2Cache, EvictionOrder.ACCESS);
	
	private static final class SessionStop implements Runnable {
		private Session session;

		public SessionStop(Session session) {
			this.session = session;
		}

		@Override
		public void run() {
			//System.out.println("STOP " + session.getId());
			SessionModeller.cache.remove(Long.toString(session.getId()));
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
			SessionModeller.cache.get(Long.toString(session.getId()));
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
			SessionModeller.cache.put(Long.toString(s.getId()), s);
			
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
				System.out.println("SIZE : " + cache.size() + " " + l2Cache.size());
			}
        	
        }, 0, 1000, TimeUnit.MILLISECONDS);
}
}
