package sobiohazardous.minestrappolation.api.item;

import java.util.List;

import sobiohazardous.minestrappolation.extradecor.lib.EDConfig;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.entity.player.UseHoeEvent;

public class MItemHoe extends ItemHoe
{	
	public MItemHoe(int par1, EnumToolMaterial par2EnumToolMaterial)
	{
        super(par1, par2EnumToolMaterial);
        this.setCreativeTab(null);
    }
	
	public void registerIcons(IconRegister r)
	{
		itemIcon = r.registerIcon("minestrappolation:" + this.getUnlocalizedName().substring(5));
	}
	
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
		if(EDConfig.showDur == true){
			 par3List.add(EnumChatFormatting.GREEN+"Durability: "+EnumChatFormatting.RED+Integer.toString(getMaxDamage()-par1ItemStack.getItemDamage()+1)+"/"+Integer.toString(getMaxDamage()+1));
		}
		 
		
    }
}
