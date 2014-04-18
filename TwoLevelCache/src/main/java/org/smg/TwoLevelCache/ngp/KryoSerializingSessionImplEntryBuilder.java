package org.smg.TwoLevelCache.ngp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.smg.TwoLevelCache.LevelTwoCacheEntryBuilder;
import org.smg.sessionmanager.cache.SessionImpl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoSerializingSessionImplEntryBuilder implements LevelTwoCacheEntryBuilder<SessionImpl> {

	Kryo kyro = new Kryo();
	public KryoSerializingSessionImplEntryBuilder() {
		kyro.register(SessionImpl.class);
		kyro.setReferences(false);
	}
	@Override
	public byte[] build(SessionImpl o) throws IOException {
		ByteArrayOutputStream bOs = new ByteArrayOutputStream();
		Output output = new Output(bOs);
		kyro.writeObject(output, o);
		output.flush();
		output.close();
		return bOs.toByteArray();
	}

	@Override
	public SessionImpl retrieve(byte[] byteArray) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream bIs = new ByteArrayInputStream(byteArray);
		
		Input input = new Input(bIs);

		SessionImpl object = (SessionImpl)kyro.readObject(input, SessionImpl.class);
		
		input.close();
		
		return object;
	}
}
