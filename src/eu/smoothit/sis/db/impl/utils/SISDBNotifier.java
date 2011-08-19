package eu.smoothit.sis.db.impl.utils;

import java.util.Observable;

public class SISDBNotifier extends Observable  {

	public static enum DB_ACTIONS {ADD, UPDATE, DELETE};
	
	
	public void setChanged() {
		super.setChanged();
	}
	
}
