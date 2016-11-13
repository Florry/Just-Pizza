package se.florry.justpizza.model.entity.block;

public class Grass extends WorldBlock
{

	public Grass()
	{
		this.modelData.color.r = 255;
		this.modelData.color.g = 255;
		this.modelData.color.b = 255;
		this.modelData.color.a = 255;

		this.indirectLightColor.r = 26;
		this.indirectLightColor.g = 130;
		this.indirectLightColor.b = 38;
		this.indirectLightColor.a = 255;
	}

	@Override
	public String getTexture()
	{
		return "blocks/dirt01Mossy";
	}

}
