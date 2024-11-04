package Game;

public class PokeEncyclopedia_dto {
	int usernum;
	int pokenum;
	int pokecount;

	public PokeEncyclopedia_dto(int usernum, int pokenum, int pokecount) {
		super();
		this.usernum = usernum;
		this.pokenum = pokenum;
		this.pokecount = pokecount;
	}

	public int getUsernum() {
		return usernum;
	}

	public void setUsernum(int usernum) {
		this.usernum = usernum;
	}

	public int getPokenum() {
		return pokenum;
	}

	public void setPokenum(int pokenum) {
		this.pokenum = pokenum;
	}

	public int getPokecount() {
		return pokecount;
	}

	public void setPokecount(int pokecount) {
		this.pokecount = pokecount;
	}
}
