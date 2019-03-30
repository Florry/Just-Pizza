package se.florry.justpizza.model.entity.block;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import se.florry.engine.material.Material;
import se.florry.engine.model.Color;
import se.florry.engine.model.QuadModel;
import se.florry.engine.model.Size;
import se.florry.justpizza.constants.GameConstants;
import se.florry.justpizza.game.Game;
import se.florry.justpizza.model.entity.Entity;
import se.florry.justpizza.model.entity.EntityInterface;

public class WorldBlock implements EntityInterface
{
	static Integer idIndex = 0;

	protected final QuadModel modelData;
	protected final Material material;
	protected final Color lightLevel;
	protected final Color indirectLightColor;
	protected boolean hasCollision;
	protected boolean isSolid;

	public final Integer id;

	public final Vector3f position;

	protected WorldBlock()
	{
		this.modelData = new QuadModel(new Size(GameConstants.Entity.WORLD_BLOCK_SIZE, GameConstants.Entity.WORLD_BLOCK_SIZE));
		this.position = new Vector3f();
		this.material = new Material(this.getTexture());
		this.lightLevel = new Color();
		this.indirectLightColor = new Color();
		this.hasCollision = true;
		this.isSolid = true;
		this.id = WorldBlock.idIndex;
		WorldBlock.idIndex++;
	}

	public Size getSize()
	{
		return this.modelData.size;
	}

	public QuadModel getModelData()
	{
		return this.modelData;
	}

	public void render()
	{
		if ((int) this.position.z == GameConstants.World.DEFAULT_Z_DEPTH || (int) this.position.z == GameConstants.World.DEFAULT_Z_DEPTH + 1)
		{
			this.modelData.position.x = this.position.x + Game.worldRenderOffset.x;
		} else
		{
			if ((int) this.position.z > GameConstants.World.DEFAULT_Z_DEPTH)
			{
				final int distance = (int) (this.position.z - GameConstants.World.DEFAULT_Z_DEPTH);
				this.modelData.position.x = this.position.x + Game.worldRenderOffset.x * distance;
			} else
			{
				final int distance = (int) (GameConstants.World.DEFAULT_Z_DEPTH - this.position.z);
				this.modelData.position.x = this.position.x + Game.worldRenderOffset.x / distance;
			}

		}

		this.modelData.position.y = this.position.y + Game.worldRenderOffset.y;

		this.material.setMaterial();

		this.modelData.color.r = this.lightLevel.r;
		this.modelData.color.g = this.lightLevel.g;
		this.modelData.color.b = this.lightLevel.b;
		this.modelData.color.a = this.lightLevel.a;

		this.modelData.render();

		this.material.disable();
	}

	public void setLightLevel(final Color lightLevel)
	{
		this.lightLevel.r = lightLevel.r;
		this.lightLevel.g = lightLevel.g;
		this.lightLevel.b = lightLevel.b;
		this.lightLevel.a = lightLevel.a;
	}

	public Color getLightLevel()
	{
		return this.lightLevel;
	}

	public void process()
	{
	}

	public Color getIndirectLightColor()
	{
		return this.indirectLightColor;
	}

	public List<Rectangle> getCollisionShape()
	{
		if (this.hasCollision)
		{
			return Arrays.asList(new Rectangle((int) this.position.x, (int) this.position.y, this.getSize().width, this.getSize().height));
		} else
		{
			return Arrays.asList();
		}
	}

	public void collide(final Entity entity)
	{
	}

	public Vector2f getGridPosition()
	{
		final Vector2f gridPosition = new Vector2f();
		gridPosition.x = (int) this.modelData.position.x / GameConstants.Entity.WORLD_BLOCK_SIZE;
		gridPosition.y = (int) this.modelData.position.y / GameConstants.Entity.WORLD_BLOCK_SIZE;

		return gridPosition;
	}

	public boolean isSolid()
	{
		return this.isSolid;
	}

	public boolean hasCollision()
	{
		return this.hasCollision;
	}

	@Override
	public String getTexture()
	{
		return "";
	}

}
