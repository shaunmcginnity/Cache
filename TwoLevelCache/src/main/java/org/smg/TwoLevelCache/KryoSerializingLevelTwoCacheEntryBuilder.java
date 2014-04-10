package org.smg.TwoLevelCache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoSerializingLevelTwoCacheEntryBuilder<T> implements
		LevelTwoCacheEntryBuilder<T> {

	@Override
	public byte[] build(T o) throws IOException {
		Kryo kyro = new Kryo();
		ByteArrayOutputStream bOs = new ByteArrayOutputStream();
		Output output = new Output(bOs);
		kyro.writeClassAndObject(output, o);
		output.flush();
		output.close();
		return bOs.toByteArray();
	}

	@Override
	public T retrieve(byte[] byteArray) throws IOException,
			ClassNotFoundException {
		Kryo kyro = new Kryo();
		ByteArrayInputStream bIs = new ByteArrayInputStream(byteArray);
		
		Input input = new Input(bIs);
		@SuppressWarnings("unchecked")
		T object = (T)kyro.readClassAndObject(input);
		
		input.close();
		
		return object;
	}

}

