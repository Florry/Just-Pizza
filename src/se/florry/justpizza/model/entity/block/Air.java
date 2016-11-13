package se.florry.justpizza.model.entity.block;

public class Air extends WorldBlock
{

	public Air()
	{
		this.modelData.color.r = 255;
		this.modelData.color.g = 255;
		this.modelData.color.b = 255;
		this.modelData.color.a = 255;
		this.modelData.setTransparency(true);
		this.isSolid = false;
	}

	@Override
	public String getTexture()
	{
		return "blocks/airTransparent";
	}

}
