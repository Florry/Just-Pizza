package se.florry.justpizza.world;

import static se.florry.justpizza.constants.GameConstants.World.DEFAULT_Z_DEPTH;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import se.florry.engine.constants.Constants;
import se.florry.engine.material.Material;
import se.florry.engine.model.Color;
import se.florry.engine.model.QuadModel;
import se.florry.engine.model.Size;
import se.florry.engine.texture.TextureLoader;
import se.florry.justpizza.constants.GameConstants;
import se.florry.justpizza.game.Game;
import se.florry.justpizza.model.entity.Entity;
import se.florry.justpizza.model.entity.block.Air;
import se.florry.justpizza.model.entity.block.Dirt;
import se.florry.justpizza.model.entity.block.Grass;
import se.florry.justpizza.model.entity.block.GrassSlope;
import se.florry.justpizza.model.entity.block.TEMP_TUNNEL;
import se.florry.justpizza.model.entity.block.WorldBlock;
import se.florry.justpizza.model.entity.block.decor.GrassDecor;

public class World
{

	private final List<List<List<WorldBlock>>> world;
	private final List<Entity> entities;
	private final QuadModel background;
	private final QuadModel backgroundClouds;
	private final Material backgroundCloudsMaterial;

	public int renderedObjects = 0;

	private int maxDepth;

	public World()
	{
		this.world = new LinkedList<>();
		this.entities = new LinkedList<>();
		this.background = new QuadModel(new Size(Constants.Display.WIDTH, Constants.Display.HEIGHT));
		this.background.color.r = 123;
		this.background.color.g = 189;
		this.background.color.b = 255;

		this.backgroundClouds = new QuadModel(new Size(1280, 285));
		this.backgroundClouds.setTransparency(true);
		this.backgroundClouds.textureSize.x = 6;
		this.backgroundClouds.position.y = 1;
		this.backgroundCloudsMaterial = new Material("background02");

	}

	public void init()
	{
		this.add(0, 0, 50, new Grass());
		// createMountains(3);
		// createMountains(5);
		loadLevelFromFile("level-test");
	}

	private void loadLevelFromFile(final String level)
	{
		BufferedImage levelImage = TextureLoader.readTextureToBufferedImage(level);

		for (int x = 0; x < levelImage.getWidth(); x++)
		{
			for (int y = 0; y < levelImage.getHeight(); y++)
			{
				Color colorOfPixel = new Color(levelImage.getRGB(x, y));

				if ((int) colorOfPixel.r == 255 && (int) colorOfPixel.g == 255 && (int) colorOfPixel.b == 255)
				{
					this.add(x, y, 2, new Grass());
				} else
				{
					if ((int) colorOfPixel.r == 255)
					{
						this.add(x, y, 0, new Dirt());
					} else if ((int) colorOfPixel.b == 255)
					{
						this.add(x, y, 0, new Grass());
						if (Math.random() > 0.5)
						{
							this.add(x, y - 1, 0, new GrassDecor());
						}
					} else
					{
						this.add(x, y, 0, new Air());
					}

					if (colorOfPixel.g == 255)
					{
						this.add(x, y, 1, new TEMP_TUNNEL());
					}
				}
			}
		}

		this.add(19, 7, 0, new GrassSlope(this));
		this.add(23, 8, 0, new GrassSlope(this));
		this.add(26, 9, 0, new GrassSlope(this));
		this.add(28, 10, 0, new GrassSlope(this));
		this.add(30, 11, 0, new GrassSlope(this));
		this.add(32, 12, 0, new GrassSlope(this));
		this.add(48, 13, 0, new GrassSlope(this));
		this.add(51, 15, 0, new GrassSlope(this));
		this.add(53, 16, 0, new GrassSlope(this));
		this.add(55, 17, 0, new GrassSlope(this));
		this.add(57, 18, 0, new GrassSlope(this));

	}

	private void createMountains(final int z)
	{
		float STEP_MAX = 1.01f;
		float STEP_CHANGE = 1.0f;
		float HEIGHT_MAX = 100;

		float height = (float) (Math.random() * HEIGHT_MAX);
		float slope = (float) ((Math.random() * STEP_MAX) * 2 - STEP_MAX);

		for (int x = 0; x < 100; x++)
		{
			height += slope;
			slope += (Math.random() * STEP_CHANGE) * 2 - STEP_CHANGE;

			if (slope > STEP_MAX)
			{
				slope = STEP_MAX;
			}
			if (slope < -STEP_MAX)
			{
				slope = -STEP_MAX;
			}

			if (height > HEIGHT_MAX)
			{
				height = HEIGHT_MAX;
				slope *= -1;
			}
			if (height < 0)
			{
				height = 0;
				slope *= -1;
			}

			for (int i = 100; i > 0; i--)
			{
				if (i + 1 > (int) height)
				{
					this.add(x, i, z, new Dirt());
				} else if (i + 2 > (int) height)
				{
					this.add(x, i, z, new Grass());
				}
			}
		}
	}

	public WorldBlock get(final int x, final int y)
	{
		if (!(x < 0 || y < 0))
		{
			if (this.world.size() > x && this.world.get(x)
					.size() > y
					&& this.world.get(x)
							.get(y)
							.size() > GameConstants.World.DEFAULT_Z_DEPTH)
			{
				return this.world.get(x)
						.get(y)
						.get(GameConstants.World.DEFAULT_Z_DEPTH);
			}
		}

		return null;
	}

	public WorldBlock get(final int x, final int y, final int zInput)
	{
		int z = DEFAULT_Z_DEPTH + zInput;

		if (!(x < 0 || y < 0 || z < 0))
		{
			if (this.world.size() > x && this.world.get(x)
					.size() > y
					&& this.world.get(x)
							.get(y)
							.size() > z)
			{
				return this.world.get(x)
						.get(y)
						.get(z);
			}
		}

		return null;
	}

	public World add(final int x, final int y, final WorldBlock worldBlock)
	{
		return add(x, y, GameConstants.World.DEFAULT_Z_DEPTH, worldBlock);
	}

	public World add(final int x, final int y, final int zInput, final WorldBlock worldBlock)
	{
		int z = DEFAULT_Z_DEPTH + zInput;

		while (this.world.size() <= x)
		{
			this.world.add(new ArrayList<>());
		}

		while (this.world.get(x)
				.size() <= y)
		{
			this.world.get(x)
					.add(new ArrayList<>());
		}

		while (this.world.get(x)
				.get(y)
				.size() <= z)
		{
			this.world.get(x)
					.get(y)
					.add(null);
		}

		this.world.get(x)
				.get(y)
				.set(z, worldBlock);

		worldBlock.position.x = x;
		worldBlock.position.y = y;
		worldBlock.position.z = z;

		if (zInput > this.maxDepth)
		{
			this.maxDepth = zInput;
		}

		return this;
	}

	public World remove(final int x, final int y)
	{
		return remove(x, y, DEFAULT_Z_DEPTH);
	}

	public World remove(final int x, final int y, final int zInput)
	{
		int z = DEFAULT_Z_DEPTH + zInput;

		if (!(x < 0 || y < 0))
		{
			if (this.world.size() > x && this.world.get(x)
					.size() > y
					&& this.world.get(x)
							.get(y)
							.size() > z)
			{
				this.world.get(x)
						.get(y)
						.remove(z);
			}
		}
		return this;
	}

	public World addEntity(Entity entity)
	{
		this.entities.add(entity);
		return this;
	}

	public World removeEntity(Entity entity)
	{
		this.entities.remove(entity);
		return this;
	}

	public List<Entity> getEntities()
	{
		return this.entities;
	}

	public int columns()
	{
		return this.world.size();
	}

	public int rows(final int column)
	{
		try
		{
			return this.world.get(column)
					.size();
		} catch (final Exception e)
		{
			return 0;
		}
	}

	public int depthBehind(final int column, final int row)
	{
		try
		{
			return this.world.get(column)
					.get(row)
					.size() - GameConstants.World.DEFAULT_Z_DEPTH;
		} catch (final Exception e)
		{
			return 0;
		}
	}

	public int depthInfront(final int column, final int row)
	{
		return GameConstants.World.DEFAULT_Z_DEPTH;
	}

	public int depth(final int column, final int row)
	{
		try
		{
			return this.world.get(column)
					.get(row)
					.size() - DEFAULT_Z_DEPTH;
		} catch (final Exception e)
		{
			return 0;
		}
	}

	private int getMaxDepth()
	{
		return this.maxDepth;
	}

	/*
	 * Processes and renders the world. Only renders what is inside the screen
	 * at any given point.
	 */
	public void process()
	{
		boolean hasRenderedEntities = false;

		this.background.render();
		this.backgroundCloudsMaterial.setMaterial();
		this.backgroundClouds.position.y = Game.worldRenderOffset.y;
		this.backgroundClouds.render();
		this.backgroundCloudsMaterial.disable();

		for (int z = 0; z < this.getMaxDepth(); z++)
		{
			int xStart = getXStart(z);
			int xLimit = getXLimit(z);
			int yStart = getYStart();
			int yLimit = getYLimit();

			for (int x = xStart; x < xLimit; x++)
			{
				for (int y = yStart; y < yLimit; y++)
				{
					if (z == GameConstants.World.DEFAULT_Z_DEPTH)
					{
						this.processPlayableArea(x, y, z);
					} else if (z != GameConstants.World.DEFAULT_Z_DEPTH - 1)
					{
						this.render(x, y, z);
					}
				}

				if (z == GameConstants.World.DEFAULT_Z_DEPTH + 1 && !hasRenderedEntities)
				{
					renderEntities();
					hasRenderedEntities = true;
				}
			}
		}

	}

	private void renderEntities()
	{
		this.entities.forEach(entity ->
		{
			entity.process();
			entity.render();
		});
	}

	public void render(final int x, final int y, final int z)
	{
		int zInput = DEFAULT_Z_DEPTH - z;

		WorldBlock current = this.get(x, y, zInput);

		if (current != null && !(current instanceof Air))
		{
			current.position.x = x * (GameConstants.Entity.WORLD_BLOCK_SIZE);
			current.position.y = y * (GameConstants.Entity.WORLD_BLOCK_SIZE);
			current.position.z = z;

			this.renderedObjects += 1;

			current.render();
		}
	}

	public void render(final int x, final int y)
	{
		this.render(x, y, GameConstants.World.DEFAULT_Z_DEPTH);
	}

	private void processPlayableArea(final int x, final int y, final int z)
	{
		WorldBlock currentBehind = this.get(x, y, 1);
		WorldBlock current = this.get(x, y);

		if (currentBehind != null && (current == null || !current.isSolid()))
		{
			currentBehind.process();
			this.render(x, y, GameConstants.World.DEFAULT_Z_DEPTH - 1);
		}
		if (current != null)
		{
			current.process();
			this.render(x, y);
		}
	}

	public int getXStart()
	{
		return (int) -Math.ceil(Game.worldRenderOffset.x / GameConstants.Entity.WORLD_BLOCK_SIZE);
	}

	public int getXStart(final int z)
	{
		int xStart = 1;

		if ((int) z == GameConstants.World.DEFAULT_Z_DEPTH || (int) z == GameConstants.World.DEFAULT_Z_DEPTH + 1)
		{
			return getXStart();
		} else
		{
			if ((int) z > GameConstants.World.DEFAULT_Z_DEPTH)
			{
				int distance = (int) (z - GameConstants.World.DEFAULT_Z_DEPTH);
				xStart = (int) -Math.ceil((Game.worldRenderOffset.x / (distance) / GameConstants.Entity.WORLD_BLOCK_SIZE));
			} else
			{
				int distance = (int) (GameConstants.World.DEFAULT_Z_DEPTH - z);
				xStart = (int) -Math.ceil((Game.worldRenderOffset.x / (distance) / GameConstants.Entity.WORLD_BLOCK_SIZE));
			}
		}

		return xStart;
	}

	public int getYStart()
	{
		return (int) -Math.ceil(Game.worldRenderOffset.y / GameConstants.Entity.WORLD_BLOCK_SIZE);
	}

	public int getXLimit()
	{
		return (int) (getXStart() + Math.ceil(Constants.Display.WIDTH / GameConstants.Entity.WORLD_BLOCK_SIZE)) + 1;
	}

	public int getXLimit(final int z)
	{
		return (int) (getXStart(z) + Math.ceil(Constants.Display.WIDTH / GameConstants.Entity.WORLD_BLOCK_SIZE)) + 1;
	}

	public int getYLimit()
	{
		return (int) (getYStart() + Math.ceil(Constants.Display.HEIGHT / GameConstants.Entity.WORLD_BLOCK_SIZE)) + 2;
	}

}
