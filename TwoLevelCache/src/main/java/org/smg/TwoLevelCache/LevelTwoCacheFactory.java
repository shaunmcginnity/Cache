package org.smg.TwoLevelCache;

class LevelTwoCacheFactory {
	static LevelTwoCache<Session> build(int id, LevelTwoCacheEntryBuilder<Session> builder, int initialCapacity) {
		switch(id) {
		case 1:
			return new InMemoryLevelTwoCache<>(builder, initialCapacity);
		case 2:
			return new FileBackedLevelTwoCache<>(builder);
		case 3:
			return new MemcacheLevelTwoCache<>(builder);
		case 4:
			return new MultithreadedMemcacheLevelTwoCache<>(builder);
		case 5:
			return new MapDBLevelTwoCache<>(builder);
		default:
			return new NotCompressingLevelTwoCache<>(initialCapacity);
		}
	}
}