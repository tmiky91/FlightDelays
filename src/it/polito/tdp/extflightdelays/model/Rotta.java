package it.polito.tdp.extflightdelays.model;

public class Rotta {
	
	private Airport idP;
	private Airport idA;
	private double media;
	
	public Rotta(Airport idP, Airport idA, double media) {
		super();
		this.idP = idP;
		this.idA = idA;
		this.media = media;
	}

	public Airport getIdP() {
		return idP;
	}

	public void setIdP(Airport idP) {
		this.idP = idP;
	}

	public Airport getIdA() {
		return idA;
	}

	public void setIdA(Airport idA) {
		this.idA = idA;
	}

	public double getMedia() {
		return media;
	}

	public void setMedia(double media) {
		this.media = media;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idA == null) ? 0 : idA.hashCode());
		result = prime * result + ((idP == null) ? 0 : idP.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rotta other = (Rotta) obj;
		if (idA == null) {
			if (other.idA != null)
				return false;
		} else if (!idA.equals(other.idA))
			return false;
		if (idP == null) {
			if (other.idP != null)
				return false;
		} else if (!idP.equals(other.idP))
			return false;
		return true;
	}
}
