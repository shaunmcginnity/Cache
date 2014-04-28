package org.smg.TwoLevelCache;

import java.util.Random;

public class NonPoolingStringPool implements StringPool {

	private final String prefix;
	private final int size;
	private final Random r = new Random();
	private boolean intern;
	
	public NonPoolingStringPool(String prefix, int size, boolean intern) {
		this.prefix = prefix;
		this.size = size;
		this.intern = intern;
	}
	
	@Override
	public int getSize() {
		return size;
	}

	@Override
	public String getStringFor(int index) {
		String string = prefix + r.nextInt(size);
		if(intern) {
			string = string.intern();
		}
		return string;
	}

	@Override
	public String getString() {
		String string = prefix + r.nextInt(size);
		if(intern) {
			string = string.intern();
		}
		return string;
	}


}
