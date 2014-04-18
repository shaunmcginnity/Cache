package org.smg.TwoLevelCache;

import java.io.IOException;
import java.util.Random;

public class SerializingTest {
	public static void main(String [] args) throws IOException, ClassNotFoundException {
		Random r = new Random();
		Session s = new Session("1234567", 98768l, null);
		for(int i=0; i<20; i++) {
			s.put("dataStartAttr" + i, r.nextInt() + "value");
		}
		
		LevelTwoCacheEntryBuilder<Session> b = new KryoSerializingSessionEntryBuilder();
		testBuilder(s, b);
		LevelTwoCacheEntryBuilder<Session> b2 = new KryoSerializingSessionEntryBuilder2();
		testBuilder(s, b2);
		LevelTwoCacheEntryBuilder<Session> d = new DefaultSerializingSessionEntryBuilder();
		testBuilder(s, d);
	}

	private static void testBuilder(Session s,
			LevelTwoCacheEntryBuilder<Session> b) throws IOException,
			ClassNotFoundException {
		
		for(int iter=0; iter<10; iter++) {
			long start = System.currentTimeMillis();
			long size = 0;
			for(int i=0; i<50000; i++) {
				byte [] bytes = b.build(s);
				size += bytes.length;
				Session out = b.retrieve(bytes);
			}
			System.out.println(System.currentTimeMillis() - start + " " + size/50000);
		}
	}
}
