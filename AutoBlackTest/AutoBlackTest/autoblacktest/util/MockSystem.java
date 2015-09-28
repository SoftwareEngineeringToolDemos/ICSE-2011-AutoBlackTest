package autoblacktest.util;

import mockit.Mock;
import mockit.MockClass;

@MockClass(realClass = System.class)
public class MockSystem {

	private long date = 1333572131312L;

	@Mock
	public long currentTimeMillis() {
		return date ;
	}
}
