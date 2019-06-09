package com.tieso2001.boneappletea.container;

import com.tieso2001.boneappletea.network.ModPacketHandler;
import com.tieso2001.boneappletea.network.PacketFluidTankUpdate;
import com.tieso2001.boneappletea.tile.TileWoodenFermentingBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerWoodenFermentingBarrel extends Container
{
    private TileWoodenFermentingBarrel tileEntity;
    private int fermentTime;
    private int maxFermentTime;

    public ContainerWoodenFermentingBarrel(IInventory playerInventory, TileWoodenFermentingBarrel tileEntity)
    {
        this.tileEntity = tileEntity;
        addTileSlots();
        addPlayerSlots(playerInventory);
    }

    private void addTileSlots()
    {
        IItemHandler itemHandler = this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        int x; int y;

        x = 26; y = 34;
        addSlotToContainer(new SlotItemHandler(itemHandler, 0, x, y));
    }

    private void addPlayerSlots(IInventory playerInventory)
    {
        // Main Inventory
        for (int row = 0; row < 3; ++row)
        {
            for (int col = 0; col < 9; ++col)
            {
                int x = 8 + col * 18;
                int y = 84 + row * 18;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
            }
        }

        // Hotbar
        for (int row = 0; row < 9; ++row)
        {
            int x = 8 + row * 18;
            int y = 142;
            this.addSlotToContainer(new Slot(playerInventory, row, x, y));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < tileEntity.slots)
            {
                if (!this.mergeItemStack(itemstack1, tileEntity.slots, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, tileEntity.slots, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return playerIn.getDistanceSq(playerIn.getPosition().add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (IContainerListener listener : listeners)
        {
            if (listener instanceof EntityPlayerMP)
            {
                if (tileEntity.getFluidTank(0).getFluid() != null) ModPacketHandler.INSTANCE.sendToAll(new PacketFluidTankUpdate(tileEntity, tileEntity.getFluidTank(0).getFluid(), 0));
                else ModPacketHandler.INSTANCE.sendToAll(new PacketFluidTankUpdate(tileEntity, new FluidStack(FluidRegistry.WATER, 0), 0));

                if (tileEntity.getFluidTank(1).getFluid() != null) ModPacketHandler.INSTANCE.sendToAll(new PacketFluidTankUpdate(tileEntity, tileEntity.getFluidTank(1).getFluid(), 1));
                else ModPacketHandler.INSTANCE.sendToAll(new PacketFluidTankUpdate(tileEntity, new FluidStack(FluidRegistry.WATER, 0), 1));
            }
        }

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener iContainerListener = this.listeners.get(i);
            if (this.fermentTime != this.tileEntity.fermentTime) iContainerListener.sendWindowProperty(this, 0, tileEntity.fermentTime);
            if (this.maxFermentTime != this.tileEntity.maxFermentTime) iContainerListener.sendWindowProperty(this, 1, tileEntity.maxFermentTime);
        }
    }

    @Override
    public void updateProgressBar(int id, int data)
    {
        if (id == 0) this.tileEntity.fermentTime = data;
        if (id == 1) this.tileEntity.maxFermentTime = data;
    }
}
