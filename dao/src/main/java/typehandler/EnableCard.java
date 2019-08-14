package typehandler;

public enum EnableCard {
	nocard(0), //没卡
	havecard(1); //有卡
	
	private final int value;

	private EnableCard(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
