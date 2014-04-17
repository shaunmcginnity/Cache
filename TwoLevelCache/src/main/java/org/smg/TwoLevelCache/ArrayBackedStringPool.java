package org.smg.TwoLevelCache;

import java.util.ArrayList;
import java.util.Random;

public class ArrayBackedStringPool implements StringPool {

	private ArrayList<String> sessionKeys;

	ArrayBackedStringPool(String prefix, int size, boolean intern) {
		sessionKeys = new ArrayList<>(size);
		
		Random r = new Random();
		for(int i=0; i<size; i++) {
			String key = prefix + r.nextInt(10000);
			if(intern) {
				key = key.intern();
			}
			sessionKeys.add(key);
		}
	}
	@Override
	public int getSize() {
		return sessionKeys.size();
	}

	@Override
	public String getStringFor(int index) {
		return sessionKeys.get(index);
	}

}
