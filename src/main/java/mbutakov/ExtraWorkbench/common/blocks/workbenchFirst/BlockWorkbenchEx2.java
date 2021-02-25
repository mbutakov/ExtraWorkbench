package mbutakov.ExtraWorkbench.common.blocks.workbenchFirst;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mbutakov.ExtraWorkbench.Main;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockWorkbenchEx2 extends Block {
	
	@SideOnly(Side.CLIENT)
	private IIcon side;
	private IIcon[] top;
	
	public BlockWorkbenchEx2() {
        super(Material.wood);
		setCreativeTab(CreativeTabs.tabDecorations);
		this.setUnlocalizedName("Universal Workbench");
		this.setTextureName("extraworkbench:wb");
	}

	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int l, float f, float f2,
			float f3) {
		   if(world.isRemote) {
			   player.openGui(Main.instance, 1, world, i, j, k);
		   }
		return true;
	}
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int i, int j) {
        return i == 1?this.top[0]:this.side;
     }
    
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister register) {
		this.top = new IIcon[1];
		this.top[0] = register.registerIcon("extraworkbench:wb_top_2");
		this.side = register.registerIcon("extraworkbench:wb_side_2");
	}
    
}
