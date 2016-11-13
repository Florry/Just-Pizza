package se.florry.justpizza.model.entity.block;

public class TEMP_TUNNEL extends WorldBlock
{
	public TEMP_TUNNEL()
	{
		this.modelData.color.r = 255;
		this.modelData.color.g = 255;
		this.modelData.color.b = 255;
		this.modelData.color.a = 255;

		this.indirectLightColor.r = 117;
		this.indirectLightColor.g = 90;
		this.indirectLightColor.b = 68;
		this.indirectLightColor.a = 255;
	}

	@Override
	public String getTexture()
	{
		return "blocks/dirtTunnel01";
	}

}
