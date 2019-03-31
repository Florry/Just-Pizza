package se.florry.justpizza.model.entity.block.decor;

import java.util.Arrays;
import java.util.List;

import se.florry.justpizza.model.entity.block.WorldBlock;

public final class GrassDecor extends WorldBlock
{

	public GrassDecor()
	{
		this.hasCollision = false;
		this.modelData.setTransparency(true);
		this.isSolid = false;
		this.blocksLight = false;
	}

	@Override
	public String getTexture()
	{
		final List<String> textures = Arrays.asList("blocks/vegetation/grassSingle01", "blocks/vegetation/grassSingle02");

		return textures.get((int) Math.round(Math.random() * (textures.size() - 1)));
	}

}
