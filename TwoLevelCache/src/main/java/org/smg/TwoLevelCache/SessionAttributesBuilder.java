package org.smg.TwoLevelCache;

import java.util.HashMap;
import java.util.Random;

final class SessionAttributesBuilder {
	private final StringPool sessionKeys;
	private final Random r;

	SessionAttributesBuilder(StringPool sessionKeys) {
		this.sessionKeys = sessionKeys;
		this.r = new Random();
	}
	
	public HashMap<String,Object> dataStartAttributes() {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		for(int i=0; i<10; i++) {
			attributes.put(sessionKeys.getStringFor(r.nextInt(sessionKeys.getSize())),
					       r.nextInt() + "value");
		}
		return attributes;
	}

	public HashMap<String,Object> initialSessionAttributes() {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		for(int i=0; i<10; i++) {
			int keyIndex = r.nextInt(sessionKeys.getSize());
			attributes.put(sessionKeys.getStringFor(keyIndex), r.nextInt() + "value");
		}
		
		return attributes;
	}
	
}