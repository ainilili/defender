package org.nico.defender.guarder;

import org.nico.defender.entity.Caller;

public interface AbstractPreventer {

	public boolean detection(Caller caller);
}
