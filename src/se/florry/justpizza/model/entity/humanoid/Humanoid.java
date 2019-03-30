package se.florry.justpizza.model.entity.humanoid;

import org.lwjgl.util.vector.Vector2f;

import se.florry.engine.input.Input;
import se.florry.engine.model.AnimatedQuadModel;
import se.florry.engine.utils.FrameUtils;
import se.florry.justpizza.constants.GameConstants;
import se.florry.justpizza.model.entity.Entity;
import se.florry.justpizza.world.World;

public final class Humanoid extends Entity
{

	public static final float ANIM_ANIMATION_STEP_SIZE = 1f / 6f;
	public static final float ANIM_WALK_RIGHT = 0;
	public static final float ANIM_WALK_LEFT = Humanoid.ANIM_ANIMATION_STEP_SIZE * 1;
	public static final float ANIM_IDLE_RIGHT = Humanoid.ANIM_ANIMATION_STEP_SIZE * 2;
	public static final float ANIM_IDLE_LEFT = Humanoid.ANIM_ANIMATION_STEP_SIZE * 3;
	public static final float ANIM_JUMP_RIGHT = Humanoid.ANIM_ANIMATION_STEP_SIZE * 4;
	public static final float ANIM_JUMP_LEFT = Humanoid.ANIM_ANIMATION_STEP_SIZE * 5;

	private final static int TOTAL_FRAMES = 4;

	private boolean isMoving;
	private int movingDirection;
	private final int movingSpeed = 10000;

	private final Vector2f MAX_VELOCITY = new Vector2f(600, 0);

	public Humanoid(final World world, final Input input)
	{
		super(new AnimatedQuadModel(GameConstants.Entity.HUMANOID_SIZE, 1, 1, 1, 2), new Vector2f(), world);

		this.isAffectedByGravity = true;
		this.modelData.setTransparency(true);

		this.modelData.frameDimensions.x = 1 / (float) Humanoid.TOTAL_FRAMES;
		this.modelData.frameDimensions.y = 1 / (float) (Humanoid.TOTAL_FRAMES + 2);
		this.modelData.textureSize.y = 1 / (float) (Humanoid.TOTAL_FRAMES + 2);

		this.modelData.setAnimationRate(6);
		this.modelData.setTotalFrames(4);
		this.modelData.startAnimation();

		this.setupBehaviours();

		this.isMoving = false;
		this.movingDirection = 0;
	}

	private void setupBehaviours()
	{
		this.behaviours.put("jump", Void ->
		{
			if (this.isAffectedByGravity)
			{
				if (!this.isFalling())
					this.velocity.y -= 400;
			} else
				this.position.y -= 10;
		});

		this.behaviours.put("duck", Void ->
		{
			if (!this.isAffectedByGravity)
				this.position.y += 10;
		});

		this.behaviours.put("walkLeft", run ->
		{
			if (this.isAffectedByGravity)
			{

				this.isMoving = true;
				this.movingDirection = -1;
			} else
			{
				this.position.x -= 10;
			}
		});

		this.behaviours.put("walkRight", Void ->
		{
			if (this.isAffectedByGravity)
			{
				this.isMoving = true;
				this.movingDirection = 1;
			} else
			{
				this.position.x += 10;
			}
		});

		this.behaviours.put("stopMoving", Void ->
		{
			this.isMoving = false;
		});

		this.behaviours.put("toggleNoClip", Void ->
		{
			if (this.isAffectedByGravity)
				this.isFalling = true;

			this.isAffectedByGravity = !this.isAffectedByGravity;
			this.shouldCollide = !this.shouldCollide;

			this.velocity.x = 0;
			this.velocity.y = 0;
		});
	}

	@Override
	public void process()
	{
		super.process();

		if (this.isMoving)
			if (Math.abs(this.velocity.x) < this.MAX_VELOCITY.x)
				this.velocity.x += this.movingDirection * this.movingSpeed * FrameUtils.getDeltaTime();

		if (this.velocity.x > 0)
			this.setAnimation(Humanoid.ANIM_WALK_RIGHT);
		else if (this.velocity.x < 0)
			this.setAnimation(Humanoid.ANIM_WALK_LEFT);
		else if (this.direction == 1)
			this.setAnimation(Humanoid.ANIM_IDLE_RIGHT);
		else
			this.setAnimation(Humanoid.ANIM_IDLE_LEFT);
	}

	@Override
	public String getTexture()
	{
		return "jonas01";
	}

}
