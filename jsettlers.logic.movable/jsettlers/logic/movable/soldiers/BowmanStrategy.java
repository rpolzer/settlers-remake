package jsettlers.logic.movable.soldiers;

import jsettlers.common.buildings.OccupyerPlace.ESoldierType;
import jsettlers.common.movable.EAction;
import jsettlers.common.movable.EDirection;
import jsettlers.common.movable.EMovableType;
import jsettlers.common.position.ISPosition2D;
import jsettlers.logic.constants.Constants;
import jsettlers.logic.movable.IMovableGrid;
import jsettlers.logic.movable.Movable;

/**
 * strategy for a bowman.
 * <p />
 * needs to follow movables and paths given by the user.
 * 
 * @author Andreas Eberle
 * 
 */
public class BowmanStrategy extends AbstractSoldierStrategy {
	private static final long serialVersionUID = 4101130971112016217L;

	public BowmanStrategy(IMovableGrid grid, Movable movable, EMovableType type) {
		super(grid, movable, type);
	}

	@Override
	protected final short getSearchRadius() {
		return Constants.BOWMAN_SEARCH_RADIUS;
	}

	@Override
	protected final void executeHit(ISPosition2D enemyPos) {
		super.setAction(EAction.ACTION1, 0.8f);
		EDirection dir = EDirection.getApproxDirection(super.getPos(), enemyPos);
		if (dir != null)
			super.setDirection(dir);

		super.getGrid().getMapObjectsManager().addArrowObject(super.getGrid().getMovable(enemyPos), super.getPos(), 0.8f);
	}

	@Override
	protected final boolean canHit(ISPosition2D enemyPos) {
		float fireRadius = super.isInTower() ? Constants.BOWMAN_FIRE_RADIUS_IN_TOWER : Constants.BOWMAN_FIRE_RADIUS;
		return Math.hypot(super.getPos().getX() - enemyPos.getX(), super.getPos().getY() - enemyPos.getY()) <= fireRadius;
	}

	@Override
	public final ESoldierType getSoldierType() {
		return ESoldierType.BOWMAN;
	}

}
