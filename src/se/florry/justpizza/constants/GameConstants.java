package se.florry.justpizza.constants;

import se.florry.engine.model.Size;

public class GameConstants
{

	public final static class Entity
	{
		public static final int WORLD_BLOCK_SIZE = 40;

		public static final Size HUMANOID_SIZE = new Size(48, 64);

		public static final float VELOCITY_FRICTION = 700f;
	}

	public final static class World
	{
		public static final int DEFAULT_Z_DEPTH = 20;
	}

	public final static class Physics
	{
		public static final float GRAVITY = 980f;

		public static final int COLLISION_CHECK_BLOCK_SIZE = 3;

		public static final int ENTITY_LIGHT_CHECK_BLOCK_SIZE = 3;
	}

}
