package sobiohazardous.minestrappolation.extraores.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import sobiohazardous.minestrappolation.api.block.MBlock;

public class BlockGlowGlass extends BlockBreakable {

	
	 public BlockGlowGlass(int par1, Material par3Material,
			boolean par4) {
		super(par1, "GlowGlass",par3Material, par4);
		
	}

	public int quantityDropped(Random par1Random)
	    {
	        return 0;
	    }

	    @SideOnly(Side.CLIENT)

	    /**
	     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
	     */
	    public int getRenderBlockPass()
	    {
	        return 1;
	    }

	    /**
	     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
	     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
	     */
	    public boolean isOpaqueCube()
	    {
	        return false;
	    }

	    /**
	     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
	     */
	    public boolean renderAsNormalBlock()
	    {
	        return false;
	    }

	    /**
	     * Return true if a player with Silk Touch can harvest this block directly, and not its normal drops.
	     */
	    protected boolean canSilkHarvest()
	    {
	        return true;
	    }
	    
	    public void registerIcons(IconRegister r)
		{
			blockIcon = r.registerIcon("minestrappolation:" + this.getUnlocalizedName().substring(5));
		}

}
