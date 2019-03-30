package se.florry.justpizza.model.entity;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.lwjgl.util.vector.Vector2f;

import se.florry.engine.handler.GenericHandler;
import se.florry.engine.material.Material;
import se.florry.engine.model.AnimatedQuadModel;
import se.florry.engine.model.Color;
import se.florry.engine.model.Size;
import se.florry.justpizza.behaviour.BehaviourHandler;
import se.florry.justpizza.constants.GameConstants;
import se.florry.justpizza.game.Game;
import se.florry.justpizza.model.entity.block.WorldBlock;
import se.florry.justpizza.physics.Collision;
import se.florry.justpizza.world.World;

public abstract class Entity implements EntityInterface
{

	private final World world;

	private final String id;
	protected final AnimatedQuadModel modelData;
	protected final Material material;
	protected final Color lightLevel;

	public final Vector2f position;
	public final Vector2f lastPostion;
	public final Vector2f velocity;
	public int direction;
	public boolean shouldCollide;

	protected boolean isAffectedByGravity;
	protected boolean isFalling;

	protected float currentAnimation;

	protected GenericHandler processHandler;

	protected final Map<String, BehaviourHandler> behaviours;

	public Entity(final AnimatedQuadModel modelData, final Vector2f position, final World world)
	{
		this.id = UUID.randomUUID()
				.toString();
		this.modelData = modelData;
		this.lightLevel = new Color(255, 255, 255);
		this.position = position;
		this.lastPostion = new Vector2f();
		this.velocity = new Vector2f();
		this.direction = 1;
		this.world = world;
		this.material = new Material(this.getTexture());
		this.behaviours = new HashMap<>();
		this.shouldCollide = true;
	}

	public void process()
	{
		final Rectangle entityRectX = new Rectangle((int) (this.position.x + this.velocity.x * Game.getDeltaTime()),
				(int) this.position.y,
				this.getSize().width,
				this.getSize().height);

		if (!this.shouldCollide || Collision.canMoveToNewPosition(entityRectX, this, this.world))
		{
			this.position.x += this.velocity.x * Game.getDeltaTime();

			if (this.velocity.x != 0)
				this.direction = (int) Math.signum(this.velocity.x);

			if (this.velocity.x > 0)
			{
				if (this.velocity.x - GameConstants.Entity.VELOCITY_FRICTION * Game.getDeltaTime() < 0)
					this.velocity.x = 0;
				else
					this.velocity.x -= GameConstants.Entity.VELOCITY_FRICTION * Game.getDeltaTime();

			} else if (this.velocity.x < 0)
				if (this.velocity.x + GameConstants.Entity.VELOCITY_FRICTION * Game.getDeltaTime() > 0)
					this.velocity.x = 0;
				else
					this.velocity.x += GameConstants.Entity.VELOCITY_FRICTION * Game.getDeltaTime();
		} else
			this.velocity.x = 0;

		final Rectangle entityRectY = new Rectangle((int) this.position.x,
				(int) (this.position.y + this.velocity.y * Game.getDeltaTime()),
				this.getSize().width,
				this.getSize().height);

		if (!this.shouldCollide || Collision.canMoveToNewPosition(entityRectY, this, this.world))
		{
			this.position.y += this.velocity.y * Game.getDeltaTime();
			this.isFalling = true;
		} else
		{
			this.velocity.y = 0;
			this.isFalling = false;
		}

		this.calculateLightLevel();

		if (this.processHandler != null)
			this.processHandler.handle(true);
	}

	public void addProcessCallbackHandler(final GenericHandler handler)
	{
		this.processHandler = handler;
	}

	private void calculateLightLevel()
	{
		final Vector2f entityGridPosition = this.getGridPosition();
		final Color lightLevel = new Color();
		int numberOfBlocks = 0;

		for (int x = (int) (entityGridPosition.x - GameConstants.Physics.COLLISION_CHECK_BLOCK_SIZE); x < entityGridPosition.x
				+ GameConstants.Physics.COLLISION_CHECK_BLOCK_SIZE; x++)
			for (int y = (int) (entityGridPosition.y - GameConstants.Physics.COLLISION_CHECK_BLOCK_SIZE); y < entityGridPosition.y
					+ GameConstants.Physics.COLLISION_CHECK_BLOCK_SIZE; y++)
			{
				final WorldBlock current = this.world.get(x, y);
				if (current != null)
				{
					numberOfBlocks++;
					final Color currentLightLevel = current.getLightLevel();

					lightLevel.r += currentLightLevel.r;
					lightLevel.g += currentLightLevel.g;
					lightLevel.b += currentLightLevel.b;
				}
			}

		lightLevel.r /= numberOfBlocks;
		lightLevel.g /= numberOfBlocks;
		lightLevel.b /= numberOfBlocks;

		this.lightLevel.r = lightLevel.r;
		this.lightLevel.g = lightLevel.g;
		this.lightLevel.b = lightLevel.b;
	}

	public void setAnimation(final float animation)
	{
		this.modelData.textureCoordinates.y = animation;
		this.currentAnimation = animation;
	}

	public void render()
	{
		this.modelData.position.x = this.position.x + Game.worldRenderOffset.x;
		this.modelData.position.y = this.position.y + Game.worldRenderOffset.y;

		this.material.setMaterial();

		this.modelData.color.r = this.lightLevel.r;
		this.modelData.color.g = this.lightLevel.g;
		this.modelData.color.b = this.lightLevel.b;
		this.modelData.color.a = 255;

		this.modelData.render();

		this.material.disable();
	}

	public Vector2f getGridPosition()
	{
		final Vector2f gridPosition = new Vector2f();
		gridPosition.x = (int) this.position.x / GameConstants.Entity.WORLD_BLOCK_SIZE;
		gridPosition.y = (int) this.position.y / GameConstants.Entity.WORLD_BLOCK_SIZE;

		return gridPosition;
	}

	public Size getSize()
	{
		return this.modelData.size;
	}

	public String getId()
	{
		return this.id;
	}

	public void setLightLevel(final Color lightLevel)
	{
		this.lightLevel.r = lightLevel.r;
		this.lightLevel.g = lightLevel.g;
		this.lightLevel.b = lightLevel.b;
		this.lightLevel.a = lightLevel.a;
	}

	public void setFalling(final boolean isFalling)
	{
		this.isFalling = isFalling;
	}

	public Color getLightLevel()
	{
		return this.lightLevel;
	}

	public boolean isAffectedByGravity()
	{
		return this.isAffectedByGravity;
	}

	@Override
	public int hashCode()
	{
		int hashCode = 37;
		hashCode *= this.modelData.hashCode();
		hashCode *= this.material.hashCode();
		hashCode *= this.lightLevel.hashCode();
		hashCode *= this.position.hashCode();

		return hashCode;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
			return true;
		else if (obj instanceof Entity)
		{
			final Entity otherEntity = (Entity) obj;
			return this.getId()
					.equals(otherEntity.getId());
		} else
			return false;
	}

	public boolean isFalling()
	{
		return this.isFalling;
	}

	public Entity execute(final String behaviour)
	{
		this.behaviours.get(behaviour)
				.execute(true);
		return this;
	}

}
