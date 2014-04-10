package org.smg.TwoLevelCache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DefaultSerializingLevelTwoCacheEntryBuilder implements LevelTwoCacheEntryBuilder<Session> {
	/* (non-Javadoc)
	 * @see org.smg.TwoLevelCache.LevelTwoCacheEntryBuilder#build(java.lang.Object)
	 */
	@Override
	public byte [] build(Session o) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(o);
		oos.close();
		return bos.toByteArray();
	}

	/* (non-Javadoc)
	 * @see org.smg.TwoLevelCache.LevelTwoCacheEntryBuilder#retrieve(byte[])
	 */
	@Override
	public Session retrieve(byte[] byteArray) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
		ObjectInputStream ois = new ObjectInputStream(bis);
		return (Session)ois.readObject();
	}
}

