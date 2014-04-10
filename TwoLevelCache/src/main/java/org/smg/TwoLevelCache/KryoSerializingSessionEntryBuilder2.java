package org.smg.TwoLevelCache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoSerializingSessionEntryBuilder2 implements LevelTwoCacheEntryBuilder<Session> {

	Kryo kyro = new Kryo();
	public KryoSerializingSessionEntryBuilder2() {
		kyro.register(Session.class);
	}
	@Override
	public byte[] build(Session o) throws IOException {
		ByteArrayOutputStream bOs = new ByteArrayOutputStream();
		Output output = new Output(bOs);
		kyro.writeObject(output, o);
		output.flush();
		output.close();
		return bOs.toByteArray();
	}

	@Override
	public Session retrieve(byte[] byteArray) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream bIs = new ByteArrayInputStream(byteArray);
		
		Input input = new Input(bIs);

		Session object = (Session)kyro.readObject(input, Session.class);
		
		input.close();
		
		return object;
	}
}
