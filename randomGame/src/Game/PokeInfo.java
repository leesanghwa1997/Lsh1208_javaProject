package Game;

public class PokeInfo {
	int pokenum;
	String name;
	String type1;
	String type2;
	String rank;
	String emergen;

	public PokeInfo(int pokenum, String name, String type1, String type2, String rank, String emergen) {
		super();
		this.pokenum = pokenum;
		this.name = name;
		this.type1 = type1;
		this.type2 = type2;
		this.rank = rank;
		this.emergen = emergen;
	}

	public int getPokenum() {
		return pokenum;
	}

	public void setPokenum(int pokenum) {
		this.pokenum = pokenum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

	public String getType2() {
		return type2;
	}

	public void setType2(String type2) {
		this.type2 = type2;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getEmergen() {
		return emergen;
	}

	public void setEmergen(String emergen) {
		this.emergen = emergen;
	}
}
