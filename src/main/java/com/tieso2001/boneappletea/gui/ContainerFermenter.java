package com.tieso2001.boneappletea.gui;

import com.tieso2001.boneappletea.gui.handler.SlotFermenterBottle;
import com.tieso2001.boneappletea.gui.handler.SlotFermenterInput;
import com.tieso2001.boneappletea.gui.handler.SlotFermenterYeast;
import com.tieso2001.boneappletea.tileentity.TileFermenter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerFermenter extends Container {

    private final TileFermenter tileEntity;

    public ContainerFermenter(IInventory playerInventory, TileFermenter tileEntity) {
        this.tileEntity = tileEntity;
        addGUISlots();
        addPlayerSlots(playerInventory);
    }

    private void addGUISlots() {
        IItemHandler itemHandler = this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        int slotIndex = 0;
        int x; int y;

        //Input Slot
        x = 79; y = 17;
        addSlotToContainer(new SlotFermenterInput(itemHandler, slotIndex++, x, y));

        //Yeast Slot
        x = 17; y = 17;
        addSlotToContainer(new SlotFermenterYeast(itemHandler, slotIndex++, x, y));

        //Bottle 1 Slot
        x = 56; y = 51;
        addSlotToContainer(new SlotFermenterBottle(itemHandler, slotIndex++, x, y));

        //Bottle 2 Slot
        x = 79; y = 58;
        addSlotToContainer(new SlotFermenterBottle(itemHandler, slotIndex++, x, y));

        //Bottle 3 Slot
        x = 102; y = 51;
        addSlotToContainer(new SlotFermenterBottle(itemHandler, slotIndex++, x, y));
    }

    private void addPlayerSlots(IInventory playerInventory) {
        // Main Inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = 84 + row * 18;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
            }
        }

        // Hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 8 + row * 18;
            int y = 142;
            this.addSlotToContainer(new Slot(playerInventory, row, x, y));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < TileFermenter.SLOTS) {
                if (!this.mergeItemStack(itemstack1, TileFermenter.SLOTS, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, TileFermenter.SLOTS, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileEntity.canInteractWith(playerIn);
    }

}

