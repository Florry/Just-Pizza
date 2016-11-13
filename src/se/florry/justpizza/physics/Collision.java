package se.florry.justpizza.physics;

import java.awt.Rectangle;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import se.florry.justpizza.constants.GameConstants;
import se.florry.justpizza.model.entity.Entity;
import se.florry.justpizza.model.entity.block.Air;
import se.florry.justpizza.model.entity.block.WorldBlock;
import se.florry.justpizza.world.World;

public final class Collision
{

	public static boolean canMoveToNewPosition(final Rectangle entityRect, final Entity entity, final World world)
	{
		final Vector2f entityGridPosition = new Vector2f(entityRect.x / GameConstants.Entity.WORLD_BLOCK_SIZE,
				entityRect.y / GameConstants.Entity.WORLD_BLOCK_SIZE);
		boolean isValid = true;

		for (int x = (int) (entityGridPosition.x - GameConstants.Physics.COLLISION_CHECK_BLOCK_SIZE); x < entityGridPosition.x
				+ GameConstants.Physics.COLLISION_CHECK_BLOCK_SIZE; x++)
		{
			for (int y = (int) (entityGridPosition.y - GameConstants.Physics.COLLISION_CHECK_BLOCK_SIZE); y < entityGridPosition.y
					+ GameConstants.Physics.COLLISION_CHECK_BLOCK_SIZE; y++)
			{
				final WorldBlock current = world.get(x, y);

				if (current != null && !(current instanceof Air))
				{
					if (isIntersecting(entityRect, current.getCollisionShape()))
					{
						isValid = false;
						current.collide(entity);
					}
				}
			}
		}

		return isValid;
	}

	private static boolean isIntersecting(final Rectangle entity, final List<Rectangle> blockCollisions)
	{
		boolean intersects = false;

		for (final Rectangle block : blockCollisions)
		{
			// o.color.r = 0;
			// o.color.g = 255;
			// o.color.b = 0;
			// o.size.width = block.width;
			// o.size.height = block.height;
			// o.position.x = block.x + Game.worldRenderOffset.x;
			// o.position.y = block.y + Game.worldRenderOffset.y;
			// o.render();

			if (entity.intersects(block))
			{
				// o.color.r = 0;
				// o.color.g = 0;
				// o.color.b = 255;
				// o.size.width = block.width;
				// o.size.height = block.height;
				// o.position.x = block.x + Game.worldRenderOffset.x;
				// o.position.y = block.y + Game.worldRenderOffset.y;
				// o.render();

				intersects = true;
			}
		}

		// o.color.r = 255;
		// o.color.g = 0;
		// o.color.b = 0;
		// o.position.x = entity.x + Game.worldRenderOffset.x;
		// o.position.y = entity.y + Game.worldRenderOffset.y;
		// o.size.width = entity.width;
		// o.size.height = entity.height;
		// o.render();

		return intersects;
	}

}
