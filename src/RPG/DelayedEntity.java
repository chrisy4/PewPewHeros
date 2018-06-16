package RPG;

import Entity.Entity;

public class DelayedEntity implements Updatable {

	private Entity e;

	private int time, count = 0;

	public DelayedEntity(Entity e, int time) {
		this.e = e;
		this.time = time;
	}

	@Override
	public void update(Level l) {
		count += l.getRefreshTime();
		if (count >= time) {
			l.addEntity(e);
			l.removeUpdatable(this);
		}
	}
}
