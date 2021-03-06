package sobiohazardous.minestrappolation.extraores.block;

import java.util.Random;




import sobiohazardous.minestrappolation.extraores.lib.EOBlockManager;
import sobiohazardous.minestrappolation.extraores.lib.EOItemManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.src.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockSteel extends Block
{
	public Icon top;
	public Icon bottom;
	
	public BlockSteel(int par1)
    {
        super(par1, Material.iron);
        this.setCreativeTab(EOBlockManager.tabOresBlocks);
    }

	public void registerIcons(IconRegister iconRegister)
	{
	         blockIcon = iconRegister.registerIcon("Minestrappolation:block_SteelSide");
	         this.top = iconRegister.registerIcon("Minestrappolation:block_SteelTop");
	         this.bottom = iconRegister.registerIcon("Minestrappolation:block_SteelBottom");
	}

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return this.blockID == EOBlockManager.BlaziumOre.blockID ? Item.blazePowder.itemID : (this.blockID == EOBlockManager.SunstoneOre.blockID ? EOItemManager.SunstoneDust.itemID : (this.blockID == EOBlockManager.SoulOre.blockID ? EOBlockManager.SoulGem.itemID : (this.blockID == EOBlockManager.PlutoniumOre.blockID ? EOItemManager.Plutonium.itemID : (this.blockID == EOBlockManager.UraniumOre.blockID ? EOItemManager.Uranium.itemID : this.blockID))));
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random)
    {
    	return this.blockID == EOBlockManager.BlaziumOre.blockID ? 2 + par1Random.nextInt(3) : (this.blockID == EOBlockManager.SunstoneOre.blockID ? 2 + par1Random.nextInt(4) : (this.blockID == EOBlockManager.SoulOre.blockID ? 2 + par1Random.nextInt(3) : (this.blockID == EOBlockManager.PlutoniumOre.blockID ? 1 + par1Random.nextInt(2) : (this.blockID == EOBlockManager.UraniumOre.blockID ? 1 + par1Random.nextInt(2) : 1))));
    }

    public int quantityDroppedWithBonus(int par1, Random par2Random)
    {
        if (par1 > 0 && this.blockID != this.idDropped(0, par2Random, par1))
        {
            int var3 = par2Random.nextInt(par1 + 2) - 1;

            if (var3 < 0)
            {
                var3 = 0;
            }

            return this.quantityDropped(par2Random) * (var3 + 1);
        }
        else
        {
            return this.quantityDropped(par2Random);
        }
    }
    
    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int par1)
    {
        return 0;
    }
    //Args: side, metadata
    
    public Icon getIcon(int i, int j)
    {
    	if (i == 0)//bottom
            
            return bottom;
    	if (i == 1)//top
           
            return top;
   
    	if (i == 2) // side
           
            return blockIcon;
    	if (i == 3)//side 
           
            return blockIcon;
    	if (i == 4) //side
   
    		return blockIcon;
    	if (i == 5) //side
   
    		return blockIcon;

    	if (j ==1)
    	{
    		return blockIcon;
    	}
		return top;
    }
    
    public boolean isBeaconBase(World worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ)
    {
        return (blockID == blockEmerald.blockID || blockID == blockGold.blockID || blockID == blockDiamond.blockID || blockID == EOBlockManager.SteelBlock.blockID || blockID == EOBlockManager.BronzeBlock.blockID || blockID == EOBlockManager.meuroditeBlock.blockID || blockID == EOBlockManager.ToriteBlock.blockID || blockID == EOBlockManager.SteelBlock.blockID || blockID == EOBlockManager.TitaniumBlock.blockID || blockID == EOBlockManager.BlaziumBlock.blockID);
    }
    
}
