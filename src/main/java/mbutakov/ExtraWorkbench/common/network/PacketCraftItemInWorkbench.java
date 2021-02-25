package mbutakov.ExtraWorkbench.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import mbutakov.ExtraWorkbench.CraftFileFirst;
import mbutakov.ExtraWorkbench.CraftFileSecond;
import mbutakov.ExtraWorkbench.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PacketCraftItemInWorkbench implements IMessage {

	public int idCraftFile;
	public int idWb;
    public PacketCraftItemInWorkbench() {}

    public PacketCraftItemInWorkbench(int idCf,int idWb) {
    	this.idCraftFile = idCf;
    	this.idWb = idWb;
    	
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.idCraftFile);
        buf.writeInt(this.idWb);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    	this.idCraftFile = buf.readInt();
    	this.idWb = buf.readInt();
    }

    public static class Handler implements IMessageHandler<PacketCraftItemInWorkbench, IMessage> {

        @Override
        public IMessage onMessage(PacketCraftItemInWorkbench packet, MessageContext message) {
        	CraftFileFirst cff;
        	CraftFileSecond cfs;
        	ItemStack[] recipe = null;
        	if(packet.idWb == 1) {
        		cff = Main.craftFile.get(packet.idCraftFile);
        		recipe = Main.recipe.get(cff);
        	}else {
        		cfs = Main.craftFileSecond.get(packet.idCraftFile);
        		recipe = Main.recipeSecond.get(cfs);
        	}
        	
        	
        	
            EntityPlayer player = message.getServerHandler().playerEntity;
		      InventoryPlayer temporaryInventory = new InventoryPlayer((EntityPlayer)null);
		      temporaryInventory.copyInventory(player.inventory);
		      boolean canCraft = true;
		          for(ItemStack recipeStack : recipe) {
		              int totalAmountFound = 0;

		              for(int n = 0; n < player.inventory.getSizeInventory(); ++n) {
		                 ItemStack stackInSlot = player.inventory.getStackInSlot(n);
		                 if(stackInSlot != null && stackInSlot.getItem() == recipeStack.getItem() && stackInSlot.getMetadata() == recipeStack.getMetadata()) {
		                    int amountFound = Math.min(stackInSlot.stackSize, recipeStack.stackSize - totalAmountFound);
		                    stackInSlot.stackSize -= amountFound;
		                    if(stackInSlot.stackSize <= 0) {
		                       stackInSlot = null;
		                    }
		                    player.inventory.setInventorySlotContents(n, stackInSlot);
		                    totalAmountFound += amountFound;
		                    if(totalAmountFound == recipeStack.stackSize) {
		                       break;
		                    }
		                 }
		              }

		              if(totalAmountFound < recipeStack.stackSize) {
		                 canCraft = false;
		                 break;
		              }

		           }
		          if(packet.idWb == 1) {
		        	  player.inventory.addItemStackToInventory(Main.craftFile.get(packet.idCraftFile).getCraftedItem());
		          }else {
		        	  player.inventory.addItemStackToInventory(Main.craftFileSecond.get(packet.idCraftFile).getCraftedItem());

		          }
		          if(!canCraft) {
		             player.inventory.copyInventory(temporaryInventory);
		          }
		          
            return null;
        }

    }

}
