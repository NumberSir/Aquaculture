package com.teammetallurgy.aquaculture.inventory.container.slot;

import com.teammetallurgy.aquaculture.item.AquaFishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotFishingRod extends SlotItemHandler {
    public ItemStackHandler rodHandler;

    public SlotFishingRod(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.setChanged(); //Initialise
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return stack.getItem() instanceof AquaFishingRodItem;
    }

    @Override
    public void setChanged() { //Save changes to the rod
        ItemStack stack = getItem();
        this.rodHandler = (ItemStackHandler) stack.getCapability(Capabilities.ItemHandler.ITEM);
        if (rodHandler == null) {
            this.rodHandler = AquaFishingRodItem.FishingRodEquipmentHandler.EMPTY;
        } else {
            if (!stack.isEmpty() && stack.hasTag() && stack.getTag() != null && stack.getTag().contains("Inventory")) {
                this.rodHandler.deserializeNBT(stack.getTag().getCompound("Inventory")); //Reload
            }
        }
    }
}